package io.github.kirillvarn.bankaccount;

public interface Mapper<C, T> {
    T mapTo(C c);
    
    C mapFrom(T t);
}
