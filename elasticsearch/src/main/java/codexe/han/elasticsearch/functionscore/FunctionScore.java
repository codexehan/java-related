package codexe.han.elasticsearch.functionscore;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

public class FunctionScore {
    public static void main(String[] args) {
        /**
         * function score
         * 默认boost_mode
         * 是multiply,但是filter分数是0，所以查询不会有变化
         * 所以改成sum
         */
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.matchAllQuery(),
                        ScoreFunctionBuilders.randomFunction().seed(10))
        };
        QueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(functions);
        ((FunctionScoreQueryBuilder) functionScoreQuery).boostMode(CombineFunction.SUM);
    }
}
