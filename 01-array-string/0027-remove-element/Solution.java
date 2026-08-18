class Solution {
public:
    int removeElement(vector<int> & nums,int val){
        int writeIndex=0;
        for (int readIndex=0;readIndex < nums.size();readIndex++){
            if(nums[readIndex]!=val){
                //if they equal remove the element in array (move the next in index to current read) and read the next value in array
               nums[writeIndex]=nums[readIndex];
             writeIndex++;
            }
        }
        return writeIndex;
    }
    /*
    Constraints
            In place
            Order of elements maybe changed
    Inputs 
        integer array nums 
        an integer val
        array length
        read variable
        write variable =k
    Operations
          remove elements which are equal to val in nums
          Example 1:
           Input: nums = [3,2,2,3], val = 3
           Output: 2, nums = [2,2,_,_]
     Psudo Code
      1. Compare first element with val
          if read value != val write to current write index, increase write Index and read the next element and increase the count of k and read
          if they equal read the next value in array
          
    Outputs
           Return k (the number of elements in nums which are not equal to val)

    */
};