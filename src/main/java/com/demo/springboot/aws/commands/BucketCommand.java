package com.demo.springboot.aws.commands;

import java.io.IOException;
import java.util.Optional;

public interface BucketCommand<T, R> {
    Optional<R> execute(T t);
}
