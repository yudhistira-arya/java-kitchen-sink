package org.yudhistiraarya.okhttp;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

class SampleAsync {
    private static final OkHttpClient client = new OkHttpClient();

    CompletableFuture<String> run() {
        final Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();

        final CompletableFuture<String> completableFuture = new CompletableFuture<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                completableFuture.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        completableFuture.completeExceptionally(new IOException("Unexpected code " + response));
                    }
                    completableFuture.complete(responseBody.string());
                }
            }
        });
        return completableFuture;
    }

    public static void main(final String[] args) throws IOException {
        new SampleAsync()
                .run()
                .exceptionally(e -> {
                    System.out.println("Error happened: " + e.getMessage());
                    return null;
                })
                .thenAccept(System.out::println)
                .join(); // don't use this in real code as it will block
        // clean up
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}
