class A {
    somaUm(x:Int) : Int {
        x <- x + 1
    };
};

class Main {
    a : A;
    b : Int;
    main() : SELF_TYPE {
        a.somaUm(1)
    };
};
