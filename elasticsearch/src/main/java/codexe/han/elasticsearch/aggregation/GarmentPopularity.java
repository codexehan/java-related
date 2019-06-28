package codexe.han.elasticsearch.aggregation;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class GarmentPopularity {
    public static void main(String[] args) {
        //  String ip = "172.28.10.55";
       // String ip = "10.60.6.147";
        String ip = "172.28.2.22";
        //  String ip = "10.60.12.174";
        String schema = "http";
        Integer port = 9200;

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));

        List<CountObject> countObjectList = readFromFile();
        countObjectList.forEach(countObject -> {
            if(countObject.getInfluencerCount()==0) countObject.setInfluencerCount(1);
        });

        /*try{
            BulkRequest bulkRequest = new BulkRequest();
            for(CountObject countObject : countObjectList){
                IndexRequest indexRequest = new IndexRequest("garment_matching_popularity").type("tags").id(countObject.getName());
                XContentBuilder builder = XContentFactory.jsonBuilder();
                builder.startObject();
                {
                    builder.field("name", countObject.getName());
                    builder.field("matching_count", countObject.getMatchingCount());
                    builder.field("influencer_count", countObject.getInfluencerCount());
                    builder.field("first_garment_id",countObject.getFirstId());
                    builder.field("second_garment_id",countObject.getSecondId());
                    builder.field("matching_influencer_ratio", countObject.getMatchingCount()/(float)countObject.getInfluencerCount());
                }
                builder.endObject();
                indexRequest.source(builder);
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //write to elasticsearch
        countObjectList.sort((o1, o2) -> {
            if((o1.getMatchingCount()/(float)o1.getInfluencerCount()) > ((o2.getMatchingCount()/(float)o2.getInfluencerCount()))){
                return 1;
            }
            else if((o1.getMatchingCount()/(float)o1.getInfluencerCount()) == ((o2.getMatchingCount()/(float)o2.getInfluencerCount()))){
                return 0;
            }
            else{
                return -1;
            }
        });
        for(int j =0; j<countObjectList.size();j++){
            CountObject countObject = countObjectList.get(j);
            if(countObject.getMatchingCount()>500) {
                // log.info("{} {} {} {} {}",i+1,countObject.getName(), countObject.getMatchingCount(), countObject.getInfluencerCount(), countObject.getMatchingCount()/(float)countObject.getInfluencerCount());
                System.out.println(String.format("%s %s %s %s %s %s %s", j + 1, countObject.getFirstId(), countObject.getSecondId(), countObject.getName(), countObject.getMatchingCount(), countObject.getInfluencerCount(), countObject.getMatchingCount() / (float) countObject.getInfluencerCount()));
            }
        }

        System.out.println("---------------------------------------------");


        /*countObjectList.sort((o1, o2) -> {
            if(o1.getMatchingCount() > o2.getMatchingCount()){
                return -1;
            }
            else if(o1.getMatchingCount() == o2.getMatchingCount()){
                return 0;
            }
            else{
                return 1;
            }
        });
        for(int j =0; j<countObjectList.size();j++){
            CountObject countObject = countObjectList.get(j);
            // log.info("{} {} {} {} {}",i+1,countObject.getName(), countObject.getMatchingCount(), countObject.getInfluencerCount(), countObject.getMatchingCount()/(float)countObject.getInfluencerCount());
            System.out.println(String.format("%s %s %s %s %s %s %s",j+1,countObject.getFirstId(),countObject.getSecondId(),countObject.getName(), countObject.getMatchingCount(), countObject.getInfluencerCount(), countObject.getMatchingCount()/(float)countObject.getInfluencerCount()));
        }*/
    }
    public static void analysis(RestHighLevelClient client){
        Map<Integer, String> garmentTable = getGarmentTable();

        Map<Long,Map<Long, CountObject>> garmentMatchingCount = new HashMap<>();

        int i = 0;
        for(Map.Entry<Integer, String> entry: garmentTable.entrySet()){
            try{
                List<Long> streetIdList = new ArrayList<>();
                List<Long> garmentMatchingList = new ArrayList<>();
                //get street snap id and amount with this garment id
                SearchRequest searchRequestStreet = new SearchRequest("deja_street_item").types("tags");
                SearchSourceBuilder searchSourceBuilderStreet = new SearchSourceBuilder();

                AggregationBuilder filterStreet = AggregationBuilders.filter("garment",QueryBuilders.termQuery("garment_id",entry.getKey()));
                AggregationBuilder aggStreet = AggregationBuilders.terms("street").field("street_for_analysis_id").size(999999);

                filterStreet.subAggregation(aggStreet);

               /* BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                boolQueryBuilder.must(QueryBuilders.termQuery("garment_id", entry.getKey()));*/
                searchSourceBuilderStreet.size(0);
                searchSourceBuilderStreet.aggregation(filterStreet);
                searchRequestStreet.source(searchSourceBuilderStreet);

                SearchResponse searchResponseStreet = client.search(searchRequestStreet, RequestOptions.DEFAULT);

                Terms res = ((ParsedFilter)searchResponseStreet.getAggregations().get("garment")).getAggregations().get("street");
                for(Terms.Bucket bucket : res.getBuckets()){
                    long streetForAnalysisId = (long) bucket.getKey();
                    streetIdList.add(streetForAnalysisId);
                }
                //get matching amount by terms agg in street snap id set
                SearchRequest searchRequestMatching = new SearchRequest("deja_street_item").types("tags");
                SearchSourceBuilder searchSourceBuilderMatching = new SearchSourceBuilder();

                filterStreet = AggregationBuilders.filter("street",QueryBuilders.termsQuery("street_for_analysis_id",streetIdList));
                aggStreet = AggregationBuilders.terms("garment").field("garment_id").size(999999);

                filterStreet.subAggregation(aggStreet);

                searchSourceBuilderMatching.size(0);
                searchSourceBuilderMatching.aggregation(filterStreet);
                searchRequestMatching.source(searchSourceBuilderMatching);

                SearchResponse searchResponseMatching = client.search(searchRequestMatching, RequestOptions.DEFAULT);

                Terms resMatching = ((ParsedFilter)searchResponseMatching.getAggregations().get("street")).getAggregations().get("garment");
                for(Terms.Bucket bucket : resMatching.getBuckets()){
                    long garmentMatchingId = (long) bucket.getKey();
                    if(garmentMatchingId != entry.getKey()){
                        garmentMatchingList.add(garmentMatchingId);

                        long firstId = garmentMatchingId<entry.getKey()?garmentMatchingId:entry.getKey();
                        long secondId = garmentMatchingId<entry.getKey()?entry.getKey():garmentMatchingId;
                        Map<Long, CountObject> secondGarmentCount = garmentMatchingCount.getOrDefault(firstId, new HashMap<>());
                        CountObject countObject = secondGarmentCount.getOrDefault(secondId, CountObject.builder()
                                .firstId(firstId)
                                .secondId(secondId)
                                .name(garmentTable.get((int)firstId)+"+"+garmentTable.get((int)secondId))
                                .build());
                        countObject.setMatchingCount((int) bucket.getDocCount());
                        secondGarmentCount.put(secondId, countObject);
                        garmentMatchingCount.put(firstId, secondGarmentCount);
                    }
                }
                //get influencer num in a matching
                for(long garmentMatchingId : garmentMatchingList){
                    long firstId = garmentMatchingId<entry.getKey()?garmentMatchingId:entry.getKey();
                    long secondId = garmentMatchingId<entry.getKey()?entry.getKey():garmentMatchingId;
                    //get matching amount by terms agg in street snap id set
                    //then try to get the influencer number
                    List<Long> matchingStreetIdList1 = new ArrayList<>();
                    List<Long> matchingStreetIdList2 = new ArrayList<>();
                    List<Long> matchingStreetIdList = new ArrayList<>();
                    //get street for analysis id first
                    SearchRequest searchRequestMatchingStreet1 = new SearchRequest("deja_street_item").types("tags");
                    SearchSourceBuilder searchSourceBuilderMatchingStreet1 = new SearchSourceBuilder();

                    filterStreet = AggregationBuilders.filter("garment",QueryBuilders.termQuery("garment_id", entry.getKey()));
                    aggStreet = AggregationBuilders.terms("street").field("street_for_analysis_id").size(999999);

                    filterStreet.subAggregation(aggStreet);

                    searchSourceBuilderMatchingStreet1.size(0);
                    searchSourceBuilderMatchingStreet1.aggregation(filterStreet);
                    searchRequestMatchingStreet1.source(searchSourceBuilderMatchingStreet1);

                    SearchResponse searchResponseMatchingStreet = client.search(searchRequestMatchingStreet1, RequestOptions.DEFAULT);

                    Terms res3 = ((ParsedFilter)searchResponseMatchingStreet.getAggregations().get("garment")).getAggregations().get("street");
                    for(Terms.Bucket bucket : res3.getBuckets()){
                        long streetForAnalysisId = (long) bucket.getKey();
                        matchingStreetIdList1.add(streetForAnalysisId);
                    }
                    //get street id of another garment id
                    SearchRequest searchRequestMatchingStreet2 = new SearchRequest("deja_street_item").types("tags");
                    SearchSourceBuilder searchSourceBuilderMatchingStreet2 = new SearchSourceBuilder();

                    filterStreet = AggregationBuilders.filter("garment",QueryBuilders.termQuery("garment_id", garmentMatchingId));
                    aggStreet = AggregationBuilders.terms("street").field("street_for_analysis_id").size(999999);

                    filterStreet.subAggregation(aggStreet);

                    searchSourceBuilderMatchingStreet2.size(0);
                    searchSourceBuilderMatchingStreet2.aggregation(filterStreet);
                    searchRequestMatchingStreet2.source(searchSourceBuilderMatchingStreet2);

                    SearchResponse searchResponseMatchingStreet2 = client.search(searchRequestMatchingStreet2, RequestOptions.DEFAULT);

                    Terms res4 = ((ParsedFilter)searchResponseMatchingStreet2.getAggregations().get("garment")).getAggregations().get("street");
                    for(Terms.Bucket bucket : res4.getBuckets()){
                        long streetForAnalysisId = (long) bucket.getKey();
                        matchingStreetIdList2.add(streetForAnalysisId);
                    }
                    if(matchingStreetIdList1.size()>matchingStreetIdList2.size()){
                        matchingStreetIdList1.retainAll(matchingStreetIdList2);
                        matchingStreetIdList = matchingStreetIdList1;
                    }
                    else {
                        matchingStreetIdList2.retainAll(matchingStreetIdList1);
                        matchingStreetIdList = matchingStreetIdList2;
                    }
                    garmentMatchingCount.get(firstId).get(secondId).setStreetCount(matchingStreetIdList.size());

                    //influencer
                    SearchRequest searchRequestMatchingInfluencer = new SearchRequest("deja_street_for_analysis").types("tags");
                    SearchSourceBuilder searchSourceBuilderMatchingInfluencer = new SearchSourceBuilder();

                    filterStreet = AggregationBuilders.filter("garment",QueryBuilders.termsQuery("street_for_analysis_id", matchingStreetIdList));
                    aggStreet = AggregationBuilders.terms("street").field("influencer_id").size(999999);

                    filterStreet.subAggregation(aggStreet);

                    searchSourceBuilderMatchingInfluencer.size(0);
                    searchSourceBuilderMatchingInfluencer.aggregation(filterStreet);
                    searchRequestMatchingInfluencer.source(searchSourceBuilderMatchingInfluencer);

                    SearchResponse searchResponseMatchingInfluencer = client.search(searchRequestMatchingInfluencer, RequestOptions.DEFAULT);

                    Terms res5 = ((ParsedFilter)searchResponseMatchingInfluencer.getAggregations().get("garment")).getAggregations().get("street");
                    if(res5.getBuckets().isEmpty()){
                        System.out.println("street for analysis id list is "+matchingStreetIdList);//1151463
                    }
                    garmentMatchingCount.get(firstId).get(secondId).setInfluencerCount(res5.getBuckets().size());
                   /* for(Terms.Bucket bucket : res4.getBuckets()){
                        int influecnerCount = (int) bucket.getDocCount();
                        garmentMatchingCount.get(firstId).get(secondId).setInfluencerCount(influecnerCount);

                    }*/


                }//get all the influencer count
                log.info("page {} done",++i);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        //sort map
        List<CountObject> countObjectList = new ArrayList<>();
        for(Long id1 : garmentMatchingCount.keySet()){
            for(Map.Entry entry : garmentMatchingCount.get(id1).entrySet()){
                countObjectList.add((CountObject) entry.getValue());
            }
        }
        writeToFile(countObjectList);
    }

    public static void writeToFile(List<CountObject> countObjectList){
        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            for(CountObject obj : countObjectList){
                o.writeObject(obj);
            }

            o.close();
            f.close();

            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

           /* // Read objects
            CountObject pr1 = (CountObject) oi.readObject();
            CountObject pr2 = (CountObject) oi.readObject();

            System.out.println(pr1.toString());
            System.out.println(pr2.toString());

            oi.close();
            fi.close();*/

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }
    public static List<CountObject> readFromFile(){
        List<CountObject> countObjectList = new ArrayList<>();
        try {
            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            int count = 5000;
            while(count-->0) {
                CountObject pr1 = (CountObject) oi.readObject();
                countObjectList.add(pr1);
            }

            oi.close();
            fi.close();
            return countObjectList;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return countObjectList;
    }





    public static Map<Integer, String> getGarmentTable(){
        Map<Integer, String> garmentNameMap = new HashMap<>();
        garmentNameMap.put(1, "red Blouse & Shirt");
        garmentNameMap.put(2, "red T-Shirt");
        garmentNameMap.put(3, "red Tunic");
        garmentNameMap.put(4, "red Cropped Top");
        garmentNameMap.put(5, "red Straight Skirt");
        garmentNameMap.put(6, "red A-Line Skirt");
        garmentNameMap.put(7, "red Jacket");
        garmentNameMap.put(8, "red Coat");
        garmentNameMap.put(9, "red Blazer");
        garmentNameMap.put(10,"red Short");
        garmentNameMap.put(11,"red Bodycon & Sheath Dress");
        garmentNameMap.put(12,"red A-line Dress");
        garmentNameMap.put(13,"red Tank Top");
        garmentNameMap.put(14,"red Bodycon Skirt");
        garmentNameMap.put(15,"red Vest");
        garmentNameMap.put(16,"red Sweater");
        garmentNameMap.put(17,"red Cardigan");
        garmentNameMap.put(18,"red Joggers & Legging");
        garmentNameMap.put(19,"red Pant");
        garmentNameMap.put(20,"red Jean");
        garmentNameMap.put(21,"red Playsuit");
        garmentNameMap.put(22,"red Jumpsuit");
        garmentNameMap.put(23,"red Shift & Shirt Dress");
        garmentNameMap.put(24,"red Bodysuit");
        garmentNameMap.put(25,"pink Blouse & Shirt");
        garmentNameMap.put(26,"pink T-Shirt");
        garmentNameMap.put(27,"pink Tunic");
        garmentNameMap.put(28,"pink Cropped Top");
        garmentNameMap.put(29,"pink Straight Skirt");
        garmentNameMap.put(30,"pink A-Line Skirt");
        garmentNameMap.put(31,"pink Jacket");
        garmentNameMap.put(32,"pink Coat");
        garmentNameMap.put(33,"pink Blazer");
        garmentNameMap.put(34,"pink Short");
        garmentNameMap.put(35,"pink Bodycon & Sheath Dress");
        garmentNameMap.put(36,"pink A-line Dress");
        garmentNameMap.put(37,"pink Tank Top");
        garmentNameMap.put(38,"pink Bodycon Skirt");
        garmentNameMap.put(39,"pink Vest");
        garmentNameMap.put(40,"pink Sweater");
        garmentNameMap.put(41,"pink Cardigan");
        garmentNameMap.put(42,"pink Joggers & Legging");
        garmentNameMap.put(43,"pink Pant");
        garmentNameMap.put(44,"pink Jean");
        garmentNameMap.put(45,"pink Playsuit");
        garmentNameMap.put(46,"pink Jumpsuit");
        garmentNameMap.put(47,"pink Shift & Shirt Dress");
        garmentNameMap.put(48,"pink Bodysuit");
        garmentNameMap.put(49,"nude Blouse & Shirt");
        garmentNameMap.put(50,"nude T-Shirt");
        garmentNameMap.put(51,"nude Tunic");
        garmentNameMap.put(52,"nude Cropped Top");
        garmentNameMap.put(53,"nude Straight Skirt");
        garmentNameMap.put(54,"nude A-Line Skirt");
        garmentNameMap.put(55,"nude Jacket");
        garmentNameMap.put(56,"nude Coat");
        garmentNameMap.put(57,"nude Blazer");
        garmentNameMap.put(58,"nude Short");
        garmentNameMap.put(59,"nude Bodycon & Sheath Dress");
        garmentNameMap.put(60,"nude A-line Dress");
        garmentNameMap.put(61,"nude Tank Top");
        garmentNameMap.put(62,"nude Bodycon Skirt");
        garmentNameMap.put(63,"nude Vest");
        garmentNameMap.put(64,"nude Sweater");
        garmentNameMap.put(65,"nude Cardigan");
        garmentNameMap.put(66,"nude Joggers & Legging");
        garmentNameMap.put(67,"nude Pant");
        garmentNameMap.put(68,"nude Jean");
        garmentNameMap.put(69,"nude Playsuit");
        garmentNameMap.put(70,"nude Jumpsuit");
        garmentNameMap.put(71,"nude Shift & Shirt Dress");
        garmentNameMap.put(72,"nude Bodysuit");
        garmentNameMap.put(73,"orange Blouse & Shirt");
        garmentNameMap.put(74,"orange T-Shirt");
        garmentNameMap.put(75,"orange Tunic");
        garmentNameMap.put(76,"orange Cropped Top");
        garmentNameMap.put(77,"orange Straight Skirt");
        garmentNameMap.put(78,"orange A-Line Skirt");
        garmentNameMap.put(79,"orange Jacket");
        garmentNameMap.put(80,"orange Coat");
        garmentNameMap.put(81,"orange Blazer");
        garmentNameMap.put(82,"orange Short");
        garmentNameMap.put(83,"orange Bodycon & Sheath Dress");
        garmentNameMap.put(84,"orange A-line Dress");
        garmentNameMap.put(85,"orange Tank Top");
        garmentNameMap.put(86,"orange Bodycon Skirt");
        garmentNameMap.put(87,"orange Vest");
        garmentNameMap.put(88,"orange Sweater");
        garmentNameMap.put(89,"orange Cardigan");
        garmentNameMap.put(90,"orange Joggers & Legging");
        garmentNameMap.put(91,"orange Pant");
        garmentNameMap.put(92,"orange Jean");
        garmentNameMap.put(93,"orange Playsuit");
        garmentNameMap.put(94,"orange Jumpsuit");
        garmentNameMap.put(95,"orange Shift & Shirt Dress");
        garmentNameMap.put(96,"orange Bodysuit");
        garmentNameMap.put(97,"yellow Blouse & Shirt");
        garmentNameMap.put(98,"yellow T-Shirt");
        garmentNameMap.put(99,"yellow Tunic");
        garmentNameMap.put(100,"yellow Cropped Top");
        garmentNameMap.put(101,"yellow Straight Skirt");
        garmentNameMap.put(102,"yellow A-Line Skirt");
        garmentNameMap.put(103,"yellow Jacket");
        garmentNameMap.put(104,"yellow Coat");
        garmentNameMap.put(105,"yellow Blazer");
        garmentNameMap.put(106,"yellow Short");
        garmentNameMap.put(107,"yellow Bodycon & Sheath Dress");
        garmentNameMap.put(108,"yellow A-line Dress");
        garmentNameMap.put(109,"yellow Tank Top");
        garmentNameMap.put(110,"yellow Bodycon Skirt");
        garmentNameMap.put(111,"yellow Vest");
        garmentNameMap.put(112,"yellow Sweater");
        garmentNameMap.put(113,"yellow Cardigan");
        garmentNameMap.put(114,"yellow Joggers & Legging");
        garmentNameMap.put(115,"yellow Pant");
        garmentNameMap.put(116,"yellow Jean");
        garmentNameMap.put(117,"yellow Playsuit");
        garmentNameMap.put(118,"yellow Jumpsuit");
        garmentNameMap.put(119,"yellow Shift & Shirt Dress");
        garmentNameMap.put(120,"yellow Bodysuit");
        garmentNameMap.put(121,"blue Blouse & Shirt");
        garmentNameMap.put(122,"blue T-Shirt");
        garmentNameMap.put(123,"blue Tunic");
        garmentNameMap.put(124,"blue Cropped Top");
        garmentNameMap.put(125,"blue Straight Skirt");
        garmentNameMap.put(126,"blue A-Line Skirt");
        garmentNameMap.put(127,"blue Jacket");
        garmentNameMap.put(128,"blue Coat");
        garmentNameMap.put(129,"blue Blazer");
        garmentNameMap.put(130,"blue Short");
        garmentNameMap.put(131,"blue Bodycon & Sheath Dress");
        garmentNameMap.put(132,"blue A-line Dress");
        garmentNameMap.put(133,"blue Tank Top");
        garmentNameMap.put(134,"blue Bodycon Skirt");
        garmentNameMap.put(135,"blue Vest");
        garmentNameMap.put(136,"blue Sweater");
        garmentNameMap.put(137,"blue Cardigan");
        garmentNameMap.put(138,"blue Joggers & Legging");
        garmentNameMap.put(139,"blue Pant");
        garmentNameMap.put(140,"blue Jean");
        garmentNameMap.put(141,"blue Playsuit");
        garmentNameMap.put(142,"blue Jumpsuit");
        garmentNameMap.put(143,"blue Shift & Shirt Dress");
        garmentNameMap.put(144,"blue Bodysuit");
        garmentNameMap.put(145,"purple Blouse & Shirt");
        garmentNameMap.put(146,"purple T-Shirt");
        garmentNameMap.put(147,"purple Tunic");
        garmentNameMap.put(148,"purple Cropped Top");
        garmentNameMap.put(149,"purple Straight Skirt");
        garmentNameMap.put(150,"purple A-Line Skirt");
        garmentNameMap.put(151,"purple Jacket");
        garmentNameMap.put(152,"purple Coat");
        garmentNameMap.put(153,"purple Blazer");
        garmentNameMap.put(154,"purple Short");
        garmentNameMap.put(155,"purple Bodycon & Sheath Dress");
        garmentNameMap.put(156,"purple A-line Dress");
        garmentNameMap.put(157,"purple Tank Top");
        garmentNameMap.put(158,"purple Bodycon Skirt");
        garmentNameMap.put(159,"purple Vest");
        garmentNameMap.put(160,"purple Sweater");
        garmentNameMap.put(161,"purple Cardigan");
        garmentNameMap.put(162,"purple Joggers & Legging");
        garmentNameMap.put(163,"purple Pant");
        garmentNameMap.put(164,"purple Jean");
        garmentNameMap.put(165,"purple Playsuit");
        garmentNameMap.put(166,"purple Jumpsuit");
        garmentNameMap.put(167,"purple Shift & Shirt Dress");
        garmentNameMap.put(168,"purple Bodysuit");
        garmentNameMap.put(169,"brown Blouse & Shirt");
        garmentNameMap.put(170,"brown T-Shirt");
        garmentNameMap.put(171,"brown Tunic");
        garmentNameMap.put(172,"brown Cropped Top");
        garmentNameMap.put(173,"brown Straight Skirt");
        garmentNameMap.put(174,"brown A-Line Skirt");
        garmentNameMap.put(175,"brown Jacket");
        garmentNameMap.put(176,"brown Coat");
        garmentNameMap.put(177,"brown Blazer");
        garmentNameMap.put(178,"brown Short");
        garmentNameMap.put(179,"brown Bodycon & Sheath Dress");
        garmentNameMap.put(180,"brown A-line Dress");
        garmentNameMap.put(181,"brown Tank Top");
        garmentNameMap.put(182,"brown Bodycon Skirt");
        garmentNameMap.put(183,"brown Vest");
        garmentNameMap.put(184,"brown Sweater");
        garmentNameMap.put(185,"brown Cardigan");
        garmentNameMap.put(186,"brown Joggers & Legging");
        garmentNameMap.put(187,"brown Pant");
        garmentNameMap.put(188,"brown Jean");
        garmentNameMap.put(189,"brown Playsuit");
        garmentNameMap.put(190,"brown Jumpsuit");
        garmentNameMap.put(191,"brown Shift & Shirt Dress");
        garmentNameMap.put(192,"brown Bodysuit");
        garmentNameMap.put(193,"grey Blouse & Shirt");
        garmentNameMap.put(194,"grey T-Shirt");
        garmentNameMap.put(195,"grey Tunic");
        garmentNameMap.put(196,"grey Cropped Top");
        garmentNameMap.put(197,"grey Straight Skirt");
        garmentNameMap.put(198,"grey A-Line Skirt");
        garmentNameMap.put(199,"grey Jacket");
        garmentNameMap.put(200,"grey Coat");
        garmentNameMap.put(201,"grey Blazer");
        garmentNameMap.put(202,"grey Short");
        garmentNameMap.put(203,"grey Bodycon & Sheath Dress");
        garmentNameMap.put(204,"grey A-line Dress");
        garmentNameMap.put(205,"grey Tank Top");
        garmentNameMap.put(206,"grey Bodycon Skirt");
        garmentNameMap.put(207,"grey Vest");
        garmentNameMap.put(208,"grey Sweater");
        garmentNameMap.put(209,"grey Cardigan");
        garmentNameMap.put(210,"grey Joggers & Legging");
        garmentNameMap.put(211,"grey Pant");
        garmentNameMap.put(212,"grey Jean");
        garmentNameMap.put(213,"grey Playsuit");
        garmentNameMap.put(214,"grey Jumpsuit");
        garmentNameMap.put(215,"grey Shift & Shirt Dress");
        garmentNameMap.put(216,"grey Bodysuit");
        garmentNameMap.put(217,"black Blouse & Shirt");
        garmentNameMap.put(218,"black T-Shirt");
        garmentNameMap.put(219,"black Tunic");
        garmentNameMap.put(220,"black Cropped Top");
        garmentNameMap.put(221,"black Straight Skirt");
        garmentNameMap.put(222,"black A-Line Skirt");
        garmentNameMap.put(223,"black Jacket");
        garmentNameMap.put(224,"black Coat");
        garmentNameMap.put(225,"black Blazer");
        garmentNameMap.put(226,"black Short");
        garmentNameMap.put(227,"black Bodycon & Sheath Dress");
        garmentNameMap.put(228,"black A-line Dress");
        garmentNameMap.put(229,"black Tank Top");
        garmentNameMap.put(230,"black Bodycon Skirt");
        garmentNameMap.put(231,"black Vest");
        garmentNameMap.put(232,"black Sweater");
        garmentNameMap.put(233,"black Cardigan");
        garmentNameMap.put(234,"black Joggers & Legging");
        garmentNameMap.put(235,"black Pant");
        garmentNameMap.put(236,"black Jean");
        garmentNameMap.put(237,"black Playsuit");
        garmentNameMap.put(238,"black Jumpsuit");
        garmentNameMap.put(239,"black Shift & Shirt Dress");
        garmentNameMap.put(240,"black Bodysuit");
        garmentNameMap.put(241,"white & cream Blouse & Shirt");
        garmentNameMap.put(242,"white & cream T-Shirt");
        garmentNameMap.put(243,"white & cream Tunic");
        garmentNameMap.put(244,"white & cream Cropped Top");
        garmentNameMap.put(245,"white & cream Straight Skirt");
        garmentNameMap.put(246,"white & cream A-Line Skirt");
        garmentNameMap.put(247,"white & cream Jacket");
        garmentNameMap.put(248,"white & cream Coat");
        garmentNameMap.put(249,"white & cream Blazer");
        garmentNameMap.put(250,"white & cream Short");
        garmentNameMap.put(251,"white & cream Bodycon & Sheath Dress");
        garmentNameMap.put(252,"white & cream A-line Dress");
        garmentNameMap.put(253,"white & cream Tank Top");
        garmentNameMap.put(254,"white & cream Bodycon Skirt");
        garmentNameMap.put(255,"white & cream Vest");
        garmentNameMap.put(256,"white & cream Sweater");
        garmentNameMap.put(257,"white & cream Cardigan");
        garmentNameMap.put(258,"white & cream Joggers & Legging");
        garmentNameMap.put(259,"white & cream Pant");
        garmentNameMap.put(260,"white & cream Jean");
        garmentNameMap.put(261,"white & cream Playsuit");
        garmentNameMap.put(262,"white & cream Jumpsuit");
        garmentNameMap.put(263,"white & cream Shift & Shirt Dress");
        garmentNameMap.put(264,"white & cream Bodysuit");
        garmentNameMap.put(265,"floral Blouse & Shirt");
        garmentNameMap.put(266,"floral T-Shirt");
        garmentNameMap.put(267,"floral Tunic");
        garmentNameMap.put(268,"floral Cropped Top");
        garmentNameMap.put(269,"floral Straight Skirt");
        garmentNameMap.put(270,"floral A-Line Skirt");
        garmentNameMap.put(271,"floral Jacket");
        garmentNameMap.put(272,"floral Coat");
        garmentNameMap.put(273,"floral Blazer");
        garmentNameMap.put(274,"floral Short");
        garmentNameMap.put(275,"floral Bodycon & Sheath Dress");
        garmentNameMap.put(276,"floral A-line Dress");
        garmentNameMap.put(277,"floral Tank Top");
        garmentNameMap.put(278,"floral Bodycon Skirt");
        garmentNameMap.put(279,"floral Vest");
        garmentNameMap.put(280,"floral Sweater");
        garmentNameMap.put(281,"floral Cardigan");
        garmentNameMap.put(282,"floral Joggers & Legging");
        garmentNameMap.put(283,"floral Pant");
        garmentNameMap.put(284,"floral Jean");
        garmentNameMap.put(285,"floral Playsuit");
        garmentNameMap.put(286,"floral Jumpsuit");
        garmentNameMap.put(287,"floral Shift & Shirt Dress");
        garmentNameMap.put(288,"floral Bodysuit");
        garmentNameMap.put(289,"stripes Blouse & Shirt");
        garmentNameMap.put(290,"stripes T-Shirt");
        garmentNameMap.put(291,"stripes Tunic");
        garmentNameMap.put(292,"stripes Cropped Top");
        garmentNameMap.put(293,"stripes Straight Skirt");
        garmentNameMap.put(294,"stripes A-Line Skirt");
        garmentNameMap.put(295,"stripes Jacket");
        garmentNameMap.put(296,"stripes Coat");
        garmentNameMap.put(297,"stripes Blazer");
        garmentNameMap.put(298,"stripes Short");
        garmentNameMap.put(299,"stripes Bodycon & Sheath Dress");
        garmentNameMap.put(300,"stripes A-line Dress");
        garmentNameMap.put(301,"stripes Tank Top");
        garmentNameMap.put(302,"stripes Bodycon Skirt");
        garmentNameMap.put(303,"stripes Vest");
        garmentNameMap.put(304,"stripes Sweater");
        garmentNameMap.put(305,"stripes Cardigan");
        garmentNameMap.put(306,"stripes Joggers & Legging");
        garmentNameMap.put(307,"stripes Pant");
        garmentNameMap.put(308,"stripes Jean");
        garmentNameMap.put(309,"stripes Playsuit");
        garmentNameMap.put(310,"stripes Jumpsuit");
        garmentNameMap.put(311,"stripes Shift & Shirt Dress");
        garmentNameMap.put(312,"stripes Bodysuit");
        garmentNameMap.put(313,"dots Blouse & Shirt");
        garmentNameMap.put(314,"dots T-Shirt");
        garmentNameMap.put(315,"dots Tunic");
        garmentNameMap.put(316,"dots Cropped Top");
        garmentNameMap.put(317,"dots Straight Skirt");
        garmentNameMap.put(318,"dots A-Line Skirt");
        garmentNameMap.put(319,"dots Jacket");
        garmentNameMap.put(320,"dots Coat");
        garmentNameMap.put(321,"dots Blazer");
        garmentNameMap.put(322,"dots Short");
        garmentNameMap.put(323,"dots Bodycon & Sheath Dress");
        garmentNameMap.put(324,"dots A-line Dress");
        garmentNameMap.put(325,"dots Tank Top");
        garmentNameMap.put(326,"dots Bodycon Skirt");
        garmentNameMap.put(327,"dots Vest");
        garmentNameMap.put(328,"dots Sweater");
        garmentNameMap.put(329,"dots Cardigan");
        garmentNameMap.put(330,"dots Joggers & Legging");
        garmentNameMap.put(331,"dots Pant");
        garmentNameMap.put(332,"dots Jean");
        garmentNameMap.put(333,"dots Playsuit");
        garmentNameMap.put(334,"dots Jumpsuit");
        garmentNameMap.put(335,"dots Shift & Shirt Dress");
        garmentNameMap.put(336,"dots Bodysuit");
        garmentNameMap.put(337,"checks Blouse & Shirt");
        garmentNameMap.put(338,"checks T-Shirt");
        garmentNameMap.put(339,"checks Tunic");
        garmentNameMap.put(340,"checks Cropped Top");
        garmentNameMap.put(341,"checks Straight Skirt");
        garmentNameMap.put(342,"checks A-Line Skirt");
        garmentNameMap.put(343,"checks Jacket");
        garmentNameMap.put(344,"checks Coat");
        garmentNameMap.put(345,"checks Blazer");
        garmentNameMap.put(346,"checks Short");
        garmentNameMap.put(347,"checks Bodycon & Sheath Dress");
        garmentNameMap.put(348,"checks A-line Dress");
        garmentNameMap.put(349,"checks Tank Top");
        garmentNameMap.put(350,"checks Bodycon Skirt");
        garmentNameMap.put(351,"checks Vest");
        garmentNameMap.put(352,"checks Sweater");
        garmentNameMap.put(353,"checks Cardigan");
        garmentNameMap.put(354,"checks Joggers & Legging");
        garmentNameMap.put(355,"checks Pant");
        garmentNameMap.put(356,"checks Jean");
        garmentNameMap.put(357,"checks Playsuit");
        garmentNameMap.put(358,"checks Jumpsuit");
        garmentNameMap.put(359,"checks Shift & Shirt Dress");
        garmentNameMap.put(360,"checks Bodysuit");
        garmentNameMap.put(361,"color-blocking Blouse & Shirt");
        garmentNameMap.put(362,"color-blocking T-Shirt");
        garmentNameMap.put(363,"color-blocking Tunic");
        garmentNameMap.put(364,"color-blocking Cropped Top");
        garmentNameMap.put(365,"color-blocking Straight Skirt");
        garmentNameMap.put(366,"color-blocking A-Line Skirt");
        garmentNameMap.put(367,"color-blocking Jacket");
        garmentNameMap.put(368,"color-blocking Coat");
        garmentNameMap.put(369,"color-blocking Blazer");
        garmentNameMap.put(370,"color-blocking Short");
        garmentNameMap.put(371,"color-blocking Bodycon & Sheath Dress");
        garmentNameMap.put(372,"color-blocking A-line Dress");
        garmentNameMap.put(373,"color-blocking Tank Top");
        garmentNameMap.put(374,"color-blocking Bodycon Skirt");
        garmentNameMap.put(375,"color-blocking Vest");
        garmentNameMap.put(376,"color-blocking Sweater");
        garmentNameMap.put(377,"color-blocking Cardigan");
        garmentNameMap.put(378,"color-blocking Joggers & Legging");
        garmentNameMap.put(379,"color-blocking Pant");
        garmentNameMap.put(380,"color-blocking Jean");
        garmentNameMap.put(381,"color-blocking Playsuit");
        garmentNameMap.put(382,"color-blocking Jumpsuit");
        garmentNameMap.put(383,"color-blocking Shift & Shirt Dress");
        garmentNameMap.put(384,"color-blocking Bodysuit");
        garmentNameMap.put(385,"animal print Blouse & Shirt");
        garmentNameMap.put(386,"animal print T-Shirt");
        garmentNameMap.put(387,"animal print Tunic");
        garmentNameMap.put(388,"animal print Cropped Top");
        garmentNameMap.put(389,"animal print Straight Skirt");
        garmentNameMap.put(390,"animal print A-Line Skirt");
        garmentNameMap.put(391,"animal print Jacket");
        garmentNameMap.put(392,"animal print Coat");
        garmentNameMap.put(393,"animal print Blazer");
        garmentNameMap.put(394,"animal print Short");
        garmentNameMap.put(395,"animal print Bodycon & Sheath Dress");
        garmentNameMap.put(396,"animal print A-line Dress");
        garmentNameMap.put(397,"animal print Tank Top");
        garmentNameMap.put(398,"animal print Bodycon Skirt");
        garmentNameMap.put(399,"animal print Vest");
        garmentNameMap.put(400,"animal print Sweater");
        garmentNameMap.put(401,"animal print Cardigan");
        garmentNameMap.put(402,"animal print Joggers & Legging");
        garmentNameMap.put(403,"animal print Pant");
        garmentNameMap.put(404,"animal print Jean");
        garmentNameMap.put(405,"animal print Playsuit");
        garmentNameMap.put(406,"animal print Jumpsuit");
        garmentNameMap.put(407,"animal print Shift & Shirt Dress");
        garmentNameMap.put(408,"animal print Bodysuit");

        garmentNameMap.put(409, "Green Blouse & Shirt");
        garmentNameMap.put(410, "Green T-shirt");
        garmentNameMap.put(411, "Green Tunic");
        garmentNameMap.put(412, "Green Cropped Top");
        garmentNameMap.put(413, "Green Straight Skirt");
        garmentNameMap.put(414, "Green A-line Skirt");
        garmentNameMap.put(415, "Green Jacket");
        garmentNameMap.put(416, "Green Coat");
        garmentNameMap.put(417, "Green Blazer");
        garmentNameMap.put(418, "Green Short");
        garmentNameMap.put(419, "Green Bodycon & Sheath Dress");
        garmentNameMap.put(420, "Green A-line Dress");
        garmentNameMap.put(421, "Green Tank Top");
        garmentNameMap.put(422, "Green Bodycon Skirt");
        garmentNameMap.put(423, "Green Vest");
        garmentNameMap.put(424, "Green Sweater");
        garmentNameMap.put(425, "Green Cardigan");
        garmentNameMap.put(426, "Green Joggers & Legging");
        garmentNameMap.put(427, "Green Pant");
        garmentNameMap.put(428, "Green Jean");
        garmentNameMap.put(429, "Green Playsuit");
        garmentNameMap.put(430, "Green Jumpsuit");
        garmentNameMap.put(431, "Green Shift & Shirt Dress");
        garmentNameMap.put(432, "Green Bodysuit");

        return garmentNameMap;
    }
}

@Builder
@Data
class CountObject implements Comparable<CountObject>,Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    long firstId;
    long secondId;
    int matchingCount;
    int streetCount;//for 校验
    int influencerCount;

    @Override
    public int compareTo(CountObject o) {
        return (this.matchingCount/(float)influencerCount) > (o.getMatchingCount()/(float)o.getInfluencerCount())?1:-1;
    }

    /*public static void main(String[] args) {
        //test write obejct to file
        CountObject p1 = new CountObject("John", 30, 1,1,1,1);
        CountObject p2 = new CountObject("Rachel", 25, 1,1,1,1);

        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(p1);
            o.writeObject(p2);

            o.close();
            f.close();

            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            CountObject pr1 = (CountObject) oi.readObject();
            CountObject pr2 = (CountObject) oi.readObject();

            System.out.println(pr1.toString());
            System.out.println(pr2.toString());

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}

