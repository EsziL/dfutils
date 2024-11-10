package com.eszil.commands;

import com.eszil.utils.TextUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Stack;

public class calc {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("calc")
            .then(ClientCommandManager.argument("expression", StringArgumentType.greedyString())
            .executes(commandContext -> {
                MinecraftClient client = MinecraftClient.getInstance();
                String expression = StringArgumentType.getString(commandContext, "expression");

                if (client.player != null) {
                    List<Text> msg = List.of(
                            Text.of("\n"),
                            Text.of("Result: ").getWithStyle(Style.EMPTY.withColor(0xFFC475)).getFirst(),
                            Text.of(String.valueOf(calculate(expression))),
                            Text.of("\n")
                    );
                    client.player.sendMessage(TextUtils.joinTexts(msg));
                }

                return Command.SINGLE_SUCCESS;
            }))
        );
    }

    public static double calculate(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                double num = 0;
                int decimalPlace = 0;
                boolean decimalFound = false;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    c = expression.charAt(i++);
                    if (c == '.') {
                        decimalFound = true;
                        continue;
                    }
                    if (decimalFound) {
                        num += (c - '0') / Math.pow(10, decimalPlace++);
                    } else {
                        num = num * 10 + (c - '0');
                    }
                }
                numbers.push(num);
            } else if (c == '(') {
                operators.push(c);
                i++;
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    processOperator(numbers, operators);
                }
                operators.pop();
                i++;
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    processOperator(numbers, operators);
                }
                operators.push(c);
                i++;
            } else if (i + 4 <= expression.length() && expression.startsWith("sqrt", i)) {
                i += 4;
                operators.push('√');
            }
        }

        // Process the remaining operators
        while (!operators.isEmpty()) {
            processOperator(numbers, operators);
        }

        // The result will be the last number left in the stack
        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            case '√' -> 4; // sqrt has the highest precedence
            default -> -1;
        };
    }

    private static void processOperator(Stack<Double> numbers, Stack<Character> operators) {
        char operator = operators.pop();
        double b = numbers.pop();
        double a = (operator == '√') ? 0 : numbers.pop(); // If it's sqrt, there's only one operand

        double result = switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            case '^' -> Math.pow(a, b);
            case '√' -> Math.sqrt(b);
            default -> 0;
        };

        numbers.push(result);
    }

}
