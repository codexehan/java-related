package codexe.han.leetcode.escapeplan;

/**
 * 为了节省空间，用第一行和第一列做记录
 *
 * 更新的时候要注意一个问题，最后再更新第一行 和 第一列
 * 表示第一行和第一列的是同一个值[0][0]
 * 原因是 假如先更新[0][0]==0 第一行，全部变为为0，那么更新列的时候，因为第一行的数值都变成0了 导致 每一列都变成0了
 * 所以要单独记录是否更新第一行，第一列
 */
//TODO: TEST
public class escape73 {
    public void setZeroes(int[][] matrix){
        boolean fr=false, fc=false;
        int m = matrix.length;
        int n = matrix[0].length;
        for(int i =0; i<m; i++){
            for(int j=0; j<n; j++){
                if(matrix[i][j]==0){
                    if(i==0) fr=true;
                    if(j==0) fc=true;
                    matrix[0][j] =0;//列
                    matrix[i][0] =0;//行
                }
            }
        }
        //遍历列和行
        for(int i =1; i<m; i++){
            if(matrix[i][0]==0){//列为0
                for(int j=0; j<n; j++){
                    matrix[i][j]=0;
                }
            }
        }
        for(int j=1; j<n; j++){
            if(matrix[0][j]==0){//行为0
                for(int i=0; i<m; i++){
                    matrix[i][j]=0;
                }
            }
        }
        if(fc){
            for(int i=0; i<m; i++){
                matrix[i][0]=0;
            }
        }
        if(fr){
            for(int j=0; j<n; j++){
                matrix[0][j]=0;
            }
        }
    }
}
