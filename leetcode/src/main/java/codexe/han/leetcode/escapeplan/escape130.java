package codexe.han.leetcode.escapeplan;

/**
 * 找到最外层四条边的'O'，然后找到与之相连的'O'全部标记为'0'
 */
public class escape130 {
    public void solve(char[][] board) {

    }
    public void dfs(char[][] board, int i, int j){
        if(i>=board.length || j>=board[0].length || i<0 || j<0) return;
        if(board[i][j]=='O'){
            board[i][j] = 'o';
            dfs(board, i+1,j);
            dfs(board, i-1,j);
            dfs(board, i,j+1);
            dfs(board, i,j-1);
        }
    }
}
