package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个循环应该是right down left top
 * 新的循环从头开始
 */
public class escape54 {
    public List<Integer> spiralOrder(int[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        List<Integer> res = new ArrayList<>();
        getResult(res, matrix, 0,0,visited);
        return res;
    }

    /**
     * 递归的方式做是错误的，因为对于最下层的
     * @param res
     * @param matrix
     * @param currenti
     * @param currentj
     * @param visited
     */
    public void getResult(List<Integer> res, int[][] matrix, int currenti, int currentj, boolean[][] visited){
        //right->down->left->top
        //right
        if(currentj<matrix[0].length && currentj>=0  && currenti>=0 && currenti<matrix.length && !visited[currenti][currentj]){
            res.add(matrix[currenti][currentj]);
            visited[currenti][currentj] = true;
            getResult(res, matrix, currenti, currentj+1, visited);
        }
        //down
        if(currentj<matrix[0].length && currentj>=0  && currenti>=0 && currenti<matrix.length  && !visited[currenti][currentj]){
            res.add(matrix[currenti][currentj]);
            visited[currenti][currentj] = true;
            getResult(res, matrix, currenti+1, currentj, visited);
        }
        //left
        if(currentj<matrix[0].length && currentj>=0  && currenti>=0 && currenti<matrix.length  && !visited[currenti][currentj]){
            res.add(matrix[currenti][currentj]);
            visited[currenti][currentj] = true;
            getResult(res, matrix, currenti, currentj-1, visited);
        }
        //top
        if(currentj<matrix[0].length && currentj>=0  && currenti>=0 && currenti<matrix.length && !visited[currenti][currentj]){
            res.add(matrix[currenti][currentj]);
            visited[currenti][currentj] = true;
            getResult(res, matrix, currenti-1, currentj, visited);
        }
    }

    public List<Integer> spiralOrder1(int[][] matrix) {
        List<Integer> ret = new ArrayList();
        if(matrix.length == 0) return ret;
        if(matrix[0].length==0) return ret;
        int rowsComp = 0;
        int colsComp = 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        while(ret.size()<matrix.length*matrix[0].length){
            for(int col = colsComp; col < cols - colsComp; col++){
                ret.add(matrix[rowsComp][col]);
                if(ret.size()==matrix.length*matrix[0].length) return ret;
            }
            rowsComp++;
            for(int row = rowsComp; row < rows - rowsComp + 1; row++){
                ret.add(matrix[row][cols-colsComp-1]);
                if(ret.size()==matrix.length*matrix[0].length) return ret;
            }
            colsComp++;
            for(int col = cols - colsComp-1; col >= colsComp-1; col--){
                ret.add(matrix[rows-rowsComp][col]);
                if(ret.size()==matrix.length*matrix[0].length) return ret;
            }
            for(int row = rows - rowsComp-1; row >= rowsComp; row--){
                ret.add(matrix[row][colsComp-1]);
                if(ret.size()==matrix.length*matrix[0].length) return ret;
            }
        }
        return ret;
    }
}
