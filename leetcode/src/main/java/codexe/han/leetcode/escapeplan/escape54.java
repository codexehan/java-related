package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

//TODO: TEST
public class escape54 {
    public List<Integer> spiralOrder(int[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        List<Integer> res = new ArrayList<>();
        getResult(res, matrix, 0,0,visited);
        return res;
    }
    public void getResult(List<Integer> res, int[][] matrix, int currenti, int currentj, boolean[][] visited){
        //right->down->left->top
        //right
        if(currentj<matrix[0].length && !visited[currenti][currentj+1]){
            res.add(matrix[currenti][currentj]+1);
            visited[currenti][currentj+1] = true;
            getResult(res, matrix, currenti, currentj+1, visited);
        }
        //down
        if(currenti<matrix.length && !visited[currenti+1][currentj]){
            res.add(matrix[currenti+1][currentj]);
            visited[currenti+1][currentj] = true;
            getResult(res, matrix, currenti+1, currentj, visited);
        }
        //left
        if(currentj>=0 && !visited[currenti][currentj-1]){
            res.add(matrix[currenti][currentj-1]);
            visited[currenti][currentj-1] = true;
            getResult(res, matrix, currenti, currentj-1, visited);
        }
        //top
        if(currenti>=0 && !visited[currenti-1][currentj]){
            res.add(matrix[currenti-1][currentj]);
            visited[currenti-1][currentj] = true;
            getResult(res, matrix, currenti-1, currentj, visited);
        }
    }
}
