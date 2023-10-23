package com.j0schi.java_llama.services;

import com.j0schi.java_llama.config.GlobalConfig;
import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Example {

    public static void main(String... args) throws IOException {
        LlamaModel.setLogger((level, message) -> System.out.print(message));
        ModelParameters modelParams = new ModelParameters()
                .setNGpuLayers(43);
        InferenceParameters inferParams = new InferenceParameters()
                .setTemperature(0.7f)
                .setPenalizeNl(true)
                .setMirostat(InferenceParameters.MiroStat.V2)
                .setAntiPrompt("\n");

        String modelPath = GlobalConfig.modelPath;
        String system = "This is a conversation between User and Llama, a friendly chatbot.\n" +
                "Llama is helpful, kind, honest, good at writing, and never fails to answer any " +
                "requests immediately and with precision.\n";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        try (LlamaModel model = new LlamaModel(modelPath, modelParams)) {
            System.out.print(system);
            String prompt = system;
            while (true) {
                prompt += "\nUser: ";
                System.out.print("\nUser: ");
                String input = reader.readLine();
                prompt += input;
                System.out.print("Llama: ");
                prompt += "\nLlama: ";
                for (LlamaModel.Output output : model.generate(prompt, inferParams)) {
                    System.out.print(output);
                    prompt += output;
                }
            }
        }
    }
}