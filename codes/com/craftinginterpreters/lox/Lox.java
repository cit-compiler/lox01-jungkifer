package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
   private static final Interpreter interpreter = new Interpreter();
   static boolean hadError = false;
   static boolean hadRuntimeError = false;

   public Lox() {
   }

   public static void main(String[] var0) throws IOException {
      if (var0.length > 1) {
         System.out.println("Usage: jlox [script]");
         System.exit(64);
      } else if (var0.length == 1) {
         runFile(var0[0]);
      } else {
         runPrompt();
      }

   }

   private static void runFile(String var0) throws IOException {
      byte[] var1 = Files.readAllBytes(Paths.get(var0));
      run(new String(var1, Charset.defaultCharset()));
      if (hadError) {
         System.exit(65);
      }

      if (hadRuntimeError) {
         System.exit(70);
      }

   }

   private static void runPrompt() throws IOException {
      InputStreamReader var0 = new InputStreamReader(System.in);
      BufferedReader var1 = new BufferedReader(var0);

      while(true) {
         System.out.print("> ");
         String var2 = var1.readLine();
         if (var2 == null) {
            return;
         }

         run(var2);
         hadError = false;
      }
   }

   private static void run(String var0) {
      Scanner var1 = new Scanner(var0);
      List var2 = var1.scanTokens();
      Parser var3 = new Parser(var2);
      List var4 = var3.parse();
      if (!hadError) {
         Resolver var5 = new Resolver(interpreter);
         var5.resolve(var4);
         if (!hadError) {
            interpreter.interpret(var4);
         }
      }
   }

   static void error(int var0, String var1) {
      report(var0, "", var1);
   }

   private static void report(int var0, String var1, String var2) {
      System.err.println("[line " + var0 + "] Error" + var1 + ": " + var2);
      hadError = true;
   }

   static void error(Token var0, String var1) {
      if (var0.type == TokenType.EOF) {
         report(var0.line, " at end", var1);
      } else {
         report(var0.line, " at '" + var0.lexeme + "'", var1);
      }

   }

   static void runtimeError(RuntimeError var0) {
      System.err.println(var0.getMessage() + "\n[line " + var0.token.line + "]");
      hadRuntimeError = true;
   }
}