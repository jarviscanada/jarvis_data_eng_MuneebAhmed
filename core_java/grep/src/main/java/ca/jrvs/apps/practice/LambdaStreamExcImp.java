package ca.jrvs.apps.practice;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Arrays;


public class LambdaStreamExcImp implements LambdaStreamExc{
    @Override
    public Stream<String> createStrStream(String... strings) {
        return Stream.of(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return createStrStream(strings).map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        return stringStream.filter(s -> !s.contains(pattern));
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        return IntStream.of(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.rangeClosed(start, end);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.asDoubleStream().map(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(n -> n % 2 != 0);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        return message -> System.out.println(prefix + message + suffix);
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        Stream.of(messages).forEach(printer);
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        getOdd(intStream).forEach(n -> printer.accept(String.valueOf(n)));
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(List::stream).map(n -> n * n);
    }

    public static void main(String[] args) {
        LambdaStreamExcImp lsi = new LambdaStreamExcImp();
        Consumer<String> printer = lsi.getLambdaPrinter("start>", "<end");

        // Test printMessages
        String[] messages = {"hello", "world", "test"};
        lsi.printMessages(messages, printer);

        // Test printOdd
        lsi.printOdd(lsi.createIntStream(1, 10), lsi.getLambdaPrinter("odd number:", "!"));

        // Test flatNestedInt
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(4, 5, 6);
        Stream<Integer> result = lsi.flatNestedInt(Stream.of(list1, list2));
        result.forEach(System.out::println); // Should print squares of each number
    }
}
