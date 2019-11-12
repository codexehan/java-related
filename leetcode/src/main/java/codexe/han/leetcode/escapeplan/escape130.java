package codexe.han.leetcode.escapeplan;

/**
 * 找到最外层四条边的'O'，然后找到与之相连的'O'全部标记为'0'
 */
public class escape130 {
    public void solve(char[][] board) {
        int m = board.length;
        if(m ==0 ){
            return;
        }
        int n = board[0].length;

        for(int i = 0; i < m ; i++){
            if(board[i][0] == 'O'){
                dfsHelper(board, i, 0);
            }
            if(board[i][n-1] =='O'){
                dfsHelper(board, i, n-1);
            }
        }
        for(int i = 0; i < n ; i++){
            if(board[0][i] == 'O'){
                dfsHelper(board, 0, i);
            }
            if(board[m-1][i] =='O'){
                dfsHelper(board, m-1, i);
            }
        }

        for(int i = 0; i < m; i++){
            for(int j = 0; j <n ; j++){
                if(board[i][j] == 'O'){
                    board[i][j] = 'X';
                }
                else if(board[i][j] == '*'){
                    board[i][j] = 'O';
                }
            }
        }



    }
    private void dfsHelper(char[][] b, int i, int j){
        if( i < 0 || j < 0 || i >= b.length || j >= b[0].length || b[i][j] != 'O'){
            return;
        }
        b[i][j] = '*';
        dfsHelper(b, i-1, j);
        dfsHelper(b, i+1, j);
        dfsHelper(b, i, j-1);
        dfsHelper(b, i, j+1);
    }
}