package codexe.han.leetcode.shopee;

public class Redmart {
    public static void main(String[] args) {
        /**
         {4, 8, 7, 3}
         {2, 5, 9, 3}
         {6, 3, 2, 5}
         {4, 4, 1, 6}
         */
        int[][] grid = new int[][]{
                {4, 8, 7, 3},
                {2, 5, 9, 3},
                {6, 3, 2, 5},
                {4, 4, 1, 6},
        };
        Redmart redmart = new Redmart();
        System.out.println(redmart.skiingGame(grid));

    }
    public int skiingGame(int[][] grid){
        int max = Integer.MIN_VALUE;
        if(grid.length==0) return 0;
        for(int i=0;i<grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                max = Math.max(dfs(grid,new int[grid.length][grid[0].length],new boolean[grid.length][grid[0].length],i,j),max);
            }
        }
        return max;
    }
    public int dfs(int[][] grid, int[][]count, boolean[][] visited, int i, int j){
        if(visited[i][j]){
            return count[i][j];
        }
        else{
            visited[i][j] = true;
            count[i][j] = 1;
            //up
            if(i>0&&grid[i][j]>grid[i-1][j]){
                count[i][j] = Math.max(count[i][j], dfs(grid, count, visited, i-1, j)+1);
            }
            //down
            if(i<grid.length-1&&grid[i][j]>grid[i+1][j]){
                count[i][j] = Math.max(count[i][j], dfs(grid, count, visited, i+1, j)+1);
            }
            //left
            if(j>0&&grid[i][j]>grid[i][j-1]){
                count[i][j] = Math.max(count[i][j], dfs(grid, count, visited, i, j-1)+1);
            }
            //right
            if(j<grid[0].length-1&&grid[i][j]>grid[i][j+1]){
                count[i][j] = Math.max(count[i][j], dfs(grid, count, visited, i, j+1)+1);
            }
            return count[i][j];
        }
    }
    public SkiPath dfs(int[][] grid, int[][]count, boolean[][] visited, int i, int j, SkiPath[][] skiPaths) {
        if (visited[i][j]) {
            return skiPaths[i][j];
        } else {
            visited[i][j] = true;
            count[i][j] = 1;
            skiPaths[i][j] = new SkiPath(grid[i][j],null, count[i][j]);
            //up
            if (i > 0 && grid[i][j] > grid[i - 1][j]) {
                SkiPath skiPath = dfs(grid, count, visited, i - 1, j, skiPaths);
                if (skiPath.maxLen + 1 > count[i][j]) {
                    count[i][j] = Math.max(count[i][j], skiPath.maxLen + 1);
                    skiPaths[i][j].next = skiPath;
                    skiPaths[i][j].maxLen = count[i][j];
                }
            }
                //down
            if (i < grid.length - 1 && grid[i][j] > grid[i + 1][j]) {
                 SkiPath skiPath = dfs(grid, count, visited, i + 1, j,skiPaths);
                if (skiPath.maxLen + 1 > count[i][j]) {
                    count[i][j] = Math.max(count[i][j], skiPath.maxLen + 1);
                    skiPaths[i][j].next = skiPath;
                    skiPaths[i][j].maxLen = count[i][j];
                }
            }
            //left
            if (j > 0 && grid[i][j] > grid[i][j - 1]) {
                SkiPath skiPath = dfs(grid, count, visited, i , j-1,skiPaths);
                if (skiPath.maxLen + 1 > count[i][j]) {
                    count[i][j] = Math.max(count[i][j], skiPath.maxLen + 1);
                    skiPaths[i][j].next = skiPath;
                    skiPaths[i][j].maxLen = count[i][j];
                }
            }
            //right
            if (j < grid[0].length - 1 && grid[i][j] > grid[i][j + 1]) {
                SkiPath skiPath = dfs(grid, count, visited, i , j+1,skiPaths);
                if (skiPath.maxLen + 1 > count[i][j]) {
                    count[i][j] = Math.max(count[i][j], skiPath.maxLen + 1);
                    skiPaths[i][j].next = skiPath;
                    skiPaths[i][j].maxLen = count[i][j];
                }
            }
            return skiPaths[i][j];
        }
    }
    class SkiPath {
        int value;
        SkiPath next;
        int maxLen;

        public SkiPath(int value, SkiPath skiPath, int maxLen) {

        }
    }
}
