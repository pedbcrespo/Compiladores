class Ex {
    conta(i:Int) : Int {
        i <- i + 1
    };
};

class Main {
    val : Int;
    ex : Ex;
    main() : Int {
        val <- ex.conta(1)
    };
};