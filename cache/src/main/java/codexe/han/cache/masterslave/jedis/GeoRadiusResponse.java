package codexe.han.cache.masterslave.jedis;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.util.SafeEncoder;

public class GeoRadiusResponse {
  private byte[] member;
  private double distance;
  private redis.clients.jedis.GeoCoordinate coordinate;

  public GeoRadiusResponse(byte[] member) {
    this.member = member;
  }

  public void setDistance(double distance) {
    this.distance = distance;
  }

  public void setCoordinate(redis.clients.jedis.GeoCoordinate coordinate) {
    this.coordinate = coordinate;
  }

  public byte[] getMember() {
    return member;
  }

  public String getMemberByString() {
    return SafeEncoder.encode(member);
  }

  public double getDistance() {
    return distance;
  }

  public GeoCoordinate getCoordinate() {
    return coordinate;
  }
}
