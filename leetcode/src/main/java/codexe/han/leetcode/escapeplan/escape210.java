package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

public class escape210 {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] du = new int[numCourses];
        List[] courses = new ArrayList[numCourses];
        for(int i=0;i<numCourses;i++){
            courses[i] = new ArrayList();
        }
        for(int i=0;i<prerequisites.length;i++){
            du[prerequisites[i][1]]++;
            courses[prerequisites[i][0]].add(prerequisites[i][1]);
        }


    }
}
