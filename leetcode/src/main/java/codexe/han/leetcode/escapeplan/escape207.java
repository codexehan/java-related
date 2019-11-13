package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

/**
 * 这道题目是 拓扑排序
 *
 * 拓扑排序的基本原理就是，从度为0的点开始删除，如果最后全部删除完毕，则不包含cycle，如果最后没有删除完毕，只剩下度不为0的点，则说明含有cycle
 *
 * 这道题目的本质，其实就是从一个节点出发，看最后会不会在这条路径上搜索到已经搜索过的节点(会不会找到一条完全不会重复的路径-这是不对的！！！！应该是只要有cycle就不行，想象一下一个图，如果有cycle)
 */
public class escape207 {
    boolean isFinishable;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        isFinishable = true;
        List[] courses = new ArrayList[numCourses];
        boolean[] visiting = new boolean[numCourses];
        boolean[] visited = new boolean[numCourses];
        for(int i=0;i<numCourses;i++){
            courses[i] = new ArrayList();
        }
        for(int i=0;i<prerequisites.length;i++){
            courses[prerequisites[i][0]].add(prerequisites[i][1]);
        }
        for(int i=0;i<numCourses;i++){
            if(visited[i]) continue;//深度优先搜索 已经确定了 没有cycle
            check(i,courses,visiting,visited);
        }
        return isFinishable;
    }

    //深度优先搜索
    public void check(int courseNo, List<Integer>[] courses, boolean[] visiting, boolean[] visited){
        if(visiting[courseNo]){
            isFinishable = false;
            return;
        }
        visiting[courseNo] = true;
        for(Integer i: courses[courseNo]){
            if(visited[i]) continue;
            check(i,courses,visiting,visited);
        }
        visited[courseNo] = true;
        visiting[courseNo] = false;//visiting要进行重置
    }

}

/**
 There are a total of n courses you have to take, labeled from 0 to n-1.

 Some courses may have prerequisites, for example to take course 0 you have to first take course 1, which is expressed as a pair: [0,1]

 Given the total number of courses and a list of prerequisite pairs, is it possible for you to finish all courses?

 Example 1:

 Input: 2, [[1,0]]
 Output: true
 Explanation: There are a total of 2 courses to take.
 To take course 1 you should have finished course 0. So it is possible.
 Example 2:

 Input: 2, [[1,0],[0,1]]
 Output: false
 Explanation: There are a total of 2 courses to take.
 To take course 1 you should have finished course 0, and to take course 0 you should
 also have finished course 1. So it is impossible.
 */
