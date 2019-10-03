package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class escape210 {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] du = new int[numCourses];
        List<Integer>[] courses = new ArrayList[numCourses];
        for(int i=0;i<numCourses;i++){
            courses[i] = new ArrayList();
        }
        for(int i=0;i<prerequisites.length;i++){
            du[prerequisites[i][1]]++;
            courses[prerequisites[i][0]].add(prerequisites[i][1]);
        }
        LinkedList<Integer> queue = new LinkedList<>();
        for(int i=0;i<numCourses;i++){
            if(du[i]==0){
                queue.add(i);
            }
        }

        int[] res = new int[numCourses];
        int i=0;
        while(!queue.isEmpty()){
            int courseDu0 = queue.pollFirst();
            res[i++] = courseDu0;
           //update du of all the followers
            for(int course : courses[courseDu0]){
                if(--du[course]==0){
                    queue.add(course);
                }
            }
        }
        return res;
    }
}
