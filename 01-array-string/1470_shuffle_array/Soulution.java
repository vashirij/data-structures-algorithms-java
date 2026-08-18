class Solution {
    public int[] shuffle(int[] nums, int n) {
        // Create an array to hold the result
        int[] ans = new int[2 * n];
        
        // Interleave the elements:
        // x_i (nums[i]) goes to even indices: 0, 2, 4...
        // y_i (nums[i + n]) goes to odd indices: 1, 3, 5...
        for (int i = 0; i < n; i++) {
            ans[2 * i] = nums[i];
            ans[2 * i + 1] = nums[i + n];
        }
        
        return ans;
    }
}