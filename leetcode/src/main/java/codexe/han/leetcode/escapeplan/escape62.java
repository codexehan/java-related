package codexe.han.leetcode.escapeplan;

/**
 * dynamic programming
 * max[currenti][currentj] = [currenti-1][currentj] + [currenti][currentj
 */
public class escape62 {
    public int uniquePaths(int m, int n){
        int[][] pathRecord = new int[m][n];
        for(int i = 0; i<m; i++){
            for(int j =0; j<n; j++){
                int left = j==0?0:pathRecord[i][j-1];
                int top = i==0?0:pathRecord[i-1][j];
                //只有上边和左边 才可以到达
                pathRecord[i][j] = i==0&&j==0?1:left+top;
            }
        }
        return pathRecord[m-1][n-1];
    }
}
