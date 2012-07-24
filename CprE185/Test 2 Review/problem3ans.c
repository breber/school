//Write a function that reverses the order of an array.

void backwards(int arr[], int size)
{
	int temp[size];
	
	int i;
	for(i=0; i<size; i++)
	{
		temp[i] = arr[i];
	}

	for(i=0; i<size; i++)
	{
		arr[i] = temp[size-i-1];
	}	
}

