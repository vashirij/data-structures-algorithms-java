class Solution {
    public int removeDuplicates(int[] nums) {
    /*
        Constraints
                  Inplace
        Inputs
              integer array nums sorted in non-decreasing order
        Operations
                  remove the duplicates in-place such that each unique element appears only once 
                  Example 1:

                 Input: nums = [1,1,2]
                 Output: 2, nums = [1,2,_]
                 Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
                 It does not matter what you leave beyond the returned k (hence they are underscores).
        Output
              return the number of unique elements k.

        Psudo Code

        */
    int writeIndex=0;
    for (int readIndex=1;readIndex<nums.length;readIndex++)
    {
        if(nums[readIndex]!=nums[writeIndex]){
            nums[writeIndex+1]=nums[readIndex];
           writeIndex++; 
        }
    }
    return writeIndex+1;
    }
}