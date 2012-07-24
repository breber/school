int isPrime(int num) {
        int i = 0;
        for (i = 2; i < num - 1; i++) {
                if (!(num % i)) {
                        return 0;
                }
        }
        return 1;
}
