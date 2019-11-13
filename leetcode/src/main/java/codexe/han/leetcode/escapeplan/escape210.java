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

/**
 There are a total of n courses you have to take, labeled from 0 to n-1.

 Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]

 Given the total number of courses and a list of prerequisite pairs, return the ordering of courses you should take to finish all courses.

 There may be multiple correct orders, you just need to return one of them. If it is impossible to finish all courses, return an empty array.

 Example 1:

 Input: 2, [[1,0]]
 Output: [0,1]
 Explanation: There are a total of 2 courses to take. To take course 1 you should have finished
 course 0. So the correct course order is [0,1] .
 Example 2:

 Input: 4, [[1,0],[2,0],[3,1],[3,2]]
 Output: [0,1,2,3] or [0,2,1,3]
 Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both
 courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
 So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3] .

 */
