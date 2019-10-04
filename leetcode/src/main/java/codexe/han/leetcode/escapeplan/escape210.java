package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class escape210 {
    public static void main(String[] args) {
        int[] res = new int[2];
        System.out.println(res[0]);
    }
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] du = new int[numCourses];
        List<Integer>[] courses = new ArrayList[numCourses];
        for(int i=0;i<numCourses;i++){
            courses[i] = new ArrayList();
        }
        for(int i=0;i<prerequisites.length;i++){
            du[prerequisites[i][0]]++;
            courses[prerequisites[i][1]].add(prerequisites[i][0]);
        }
        LinkedList<Integer> queue = new LinkedList<>();
        for(int i=0;i<numCourses;i++){
            if(du[i]==0){
                queue.add(i);
            }
        }

        //int[] res = new int[numCourses];//不能用数组 会输出[0,0]初始化状态
        List<Integer> res = new ArrayList<>();
        while(!queue.isEmpty()){
            int courseDu0 = queue.pollFirst();
            res.add(courseDu0);
           //update du of all the followers
            for(int course : courses[courseDu0]){
                if(--du[course]==0){
                    queue.add(course);
                }
            }
        }
        //list to array 的操作会特别耗时，优化方式是使用数组，空new int[0]
        if(res.size()<numCourses) res.clear();
        return res.stream().mapToInt(Integer::intValue).toArray();
    }
}
