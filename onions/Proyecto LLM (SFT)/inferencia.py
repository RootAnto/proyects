from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig
from peft import PeftModel

model_base = "deepseek-ai/deepseek-llm-7b"
adapter_path = "modelo_finetune_lora"

bnb_config = BitsAndBytesConfig(load_in_4bit=True, bnb_4bit_compute_dtype="float16")

print("Cargando modelo y adaptadores...")
model = AutoModelForCausalLM.from_pretrained(model_base, quantization_config=bnb_config, device_map="auto")
model = PeftModel.from_pretrained(model, adapter_path)
tokenizer = AutoTokenizer.from_pretrained(model_base)

while True:
    prompt = input("\n🔐 Pregunta sobre ciberseguridad: ")
    inputs = tokenizer(prompt, return_tensors="pt").to(model.device)
    outputs = model.generate(**inputs, max_new_tokens=300)
    print("\n🧠 Respuesta:", tokenizer.decode(outputs[0], skip_special_tokens=True))
