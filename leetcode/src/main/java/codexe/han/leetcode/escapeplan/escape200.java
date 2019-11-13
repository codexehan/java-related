package codexe.han.leetcode.escapeplan;

/**
 * 统计连续的1的模块个数
 *
 * 找到一个没有访问过的1，numIslands++
 * 然后deep-first-search,标记所有相连的1
 */

public class escape200 {
    public int numIslands(char[][] grid) {
        int res = 0;
        if(grid.length!=0) {
            boolean[][] visited = new boolean[grid.length][grid[0].length];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (!visited[i][j] && grid[i][j] == '1') {
                        dfs(grid, visited, i, j);
                        res++;
                    }
                }
            }
        }
        return res;
    }
    public void dfs(char[][] grid, boolean[][] visited, int x, int y){
        if(x>=0&&x<grid.length && y>=0 && y<grid[0].length && !visited[x][y]) {
            visited[x][y] = true;
            if(grid[x][y]=='1'){
                dfs(grid,visited,x+1,y);
                dfs(grid,visited,x-1,y);
                dfs(grid,visited,x,y+1);
                dfs(grid,visited,x,y-1);
            }
        }
    }
}

/**
 Given a 2d grid map of '1's (land) and '0's (water), count the number of islands. An island is surrounded by water
 and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.

 Example 1:

 Input:
 11110
 11010
 11000
 00000

 Output: 1
 Example 2:

 Input:
 11000
 11000
 00100
 00011

 Output: 3
 */
