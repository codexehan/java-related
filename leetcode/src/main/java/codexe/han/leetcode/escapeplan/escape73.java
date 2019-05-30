package codexe.han.leetcode.escapeplan;

/**
 * 为了节省空间，用第一行和第一列做记录
 */
//TODO: TEST
public class escape73 {
    public void setZeroes(int[][] matrix){
        int m = matrix.length;
        int n = matrix[0].length;
        for(int i =0; i<m; i++){
            for(int j=0; j<n; j++){
                if(matrix[i][j]==0){
                    matrix[0][j] =0;//列
                    matrix[i][0] =0;//行
                }
            }
        }
        //遍历列和行
        for(int i =0; i<m; i++){
            if(matrix[i][0]==0){//列为0
                for(int j=0; j<n; j++){
                    matrix[i][j]=0;
                }
            }
        }
        for(int j=0; j<n; j++){
            if(matrix[0][j]==0){//行为0
                for(int i=0; i<m; i++){
                    matrix[i][j]=0;
                }
            }
        }
    }
}
