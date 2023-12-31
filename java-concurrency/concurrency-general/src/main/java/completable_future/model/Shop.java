package completable_future.model;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

   @Getter
   private final String name;

   private final Random random;

   public Shop(String name) {
      this.name = name;
      random = new Random((long) name.charAt(0) * name.charAt(1) * name.charAt(2));
   }

   public double getPrice(String product) {
      return calculatePrice(product);
   }

   private double calculatePrice(String product) {
      Util.delay();
      return random.nextDouble() * product.charAt(0) + product.charAt(1);
   }

   public Future<Double> getPriceAsync(String product) {
      CompletableFuture<Double> futurePrice = new CompletableFuture<>();
      new Thread(() -> {
         try {
            double price = getPrice(product);
            futurePrice.complete(price);
         } catch (Exception ex) {
            futurePrice.completeExceptionally(ex);
         }
      }).start();
      return futurePrice;
   }
}
