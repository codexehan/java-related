package codexe.han.leetcode.escapeplan;

public class escape79 {
    public static void main(String[] args) {

    }
    public static boolean exist(char[][] board, String word){
        boolean[][] visited = new boolean[board.length][board[0].length];
        for(int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                visited[i][j] = true;
                if(backtracking(board,visited,word,0,i,j)) return true;
                visited[i][j] = false;
            }
        }
        return false;
    }
    public static boolean backtracking(char[][] board, boolean[][] visited, String word, int start, int i, int j){
        if(board[i][j]==word.charAt(start)){
            if(start==word.length()-1) return true;
            if(j>0&&!visited[i][j-1]){
                visited[i][j-1]=true;
                if(backtracking(board, visited,word, start+1,i,j-1)) return true;
                visited[i][j-1]=false;
            }
            if(j<board[0].length-1&&!visited[i][j+1]){
                visited[i][j+1]=true;
                if(backtracking(board, visited,word, start+1,i,j+1)) return true;
                visited[i][j+1]=false;
            }
            if(i>0&&!visited[i-1][j]){
                visited[i-1][j]=true;
                if(backtracking(board, visited,word, start+1,i-1,j)) return true;
                visited[i-1][j]=false;
            }
            if(i<board.length-1&&!visited[i+1][j]){
                visited[i+1][j]=true;
                if(backtracking(board, visited,word, start+1,i+1,j)) return true;
                visited[i+1][j]=false;
            }
        }
        return false;
    }
}
