//Write a function that swaps the value of 2 input variables

void swap(int* var1, int* var2)
{

	int temp = *var1;
	*var1 = *var2;
	*var2 = temp;
}

