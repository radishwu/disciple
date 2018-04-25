package com.monster.disciple;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CacheBuilder {

    private CacheBuilder() {
    }

    public static CacheBuilder newBuilder() {
        return new CacheBuilder();
    }

    private String cacheKey;

    private final List<Type> args = new ArrayList<>();

    private Type type;

    public CacheBuilder withCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
        return this;
    }

    public CacheBuilder addType(Class... classes) {
        if (null == classes) {
            throw new NullPointerException("addType() must not null Class");
        }
        args.addAll(Arrays.asList(classes));
        return this;
    }

    public CacheBuilder build() {

        if (1 == args.size()) {
            type = args.get(0);
        } else {
            type = newInstanceType(args, 0);
        }
        return this;
    }

    public Type getType() {
        return type;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    private Type newInstanceType(List<Type> args, int index) {
        if (index + 1 == args.size() - 1) {
            return new ParameterizedTypeImpl((Class) args.get(index), new Type[]{args.get(index + 1)});
        }
        return new ParameterizedTypeImpl((Class) args.get(index), new Type[]{newInstanceType(args, index + 1)});
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {

        private Class raw;
        private Type[] args;

        ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = null != args ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
