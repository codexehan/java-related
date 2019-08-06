package codexe.han.cache.masterslave.jedis;

public abstract class Builder<T> {
  public abstract T build(Object data);
}
