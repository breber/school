//Write a function that sets the maximum and minimum values that are found
//in the given array.
//int size -> is the size of the array.

void minmax(int arr[], int* min, int* max, int size)
{
	*min = arr[0];
	*max = arr[0];

	int i;
	for(i=0; i<size; i++)
	{
		if(arr[i] < *min)
		{
			*min = arr[i];
		}
		if(arr[i] > *max)
		{
			*max = arr[i];
		}
	}
}

