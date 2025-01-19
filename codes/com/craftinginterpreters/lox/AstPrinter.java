

package com.craftinginterpreters.lox;

class AstPrinter implements Expr.Visitor<String> {
    // Expr �𕶎���ɕϊ����郁�\�b�h
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesize("assign " + expr.name.lexeme, expr.value);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme; // �ϐ��������̂܂ܕ�����Ƃ��ĕԂ�
    }

    // �e�q�֌W��\�����߂ɕ���������ʂŊ���
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    // ���C���֐��� AST �𐶐����ĕ\��
    public static void main(String[] args) {
        // AST �̍\�z��
        Expr expression = new Expr.Binary(
            new Expr.Unary(
                new Token(TokenType.MINUS, "-", null, 1),
                new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(
                new Expr.Literal(45.67)));

        // AST �𕶎���ɕϊ����ďo��
        System.out.println(new AstPrinter().print(expression));
    }
}
