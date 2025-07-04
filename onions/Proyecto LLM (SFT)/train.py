from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig
from peft import LoraConfig, get_peft_model
from trl import SFTTrainer, TrainingArguments
from datasets import load_dataset, Dataset
import json, glob

model_name = "deepseek-ai/deepseek-llm-7b"

# 1. Preparar dataset desde JSONs
all_data = []
for archivo in glob.glob("data/*.json"):
    with open(archivo, "r", encoding="utf-8") as f:
        j = json.load(f)
        prompt = f"{j['instruccion']}\n{j['input']}" if j.get('input') else j['instruccion']
        all_data.append({"prompt": prompt, "output": j["output"]})

dataset = Dataset.from_list(all_data)

# 2. Configurar cuantización 4-bit
bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_compute_dtype="float16",
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type="nf4"
)

# 3. Tokenizer y modelo base
print("Cargando modelo...")
tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
tokenizer.pad_token = tokenizer.eos_token

model = AutoModelForCausalLM.from_pretrained(
    model_name,
    quantization_config=bnb_config,
    device_map="auto",
    trust_remote_code=True
)

# 4. Configurar LoRA
lora_config = LoraConfig(
    r=8,
    lora_alpha=16,
    target_modules=["q_proj", "v_proj"],
    lora_dropout=0.1,
    bias="none",
    task_type="CAUSAL_LM"
)

model = get_peft_model(model, lora_config)

# 5. Entrenamiento
print("Entrenando modelo...")
training_args = TrainingArguments(
    output_dir="modelo_finetune_lora",
    per_device_train_batch_size=1,
    gradient_accumulation_steps=8,
    learning_rate=2e-4,
    num_train_epochs=3,
    logging_steps=10,
    save_strategy="epoch",
    fp16=True
)

trainer = SFTTrainer(
    model=model,
    tokenizer=tokenizer,
    train_dataset=dataset,
    args=training_args,
    dataset_text_field="prompt",
    max_seq_length=512,
    packing=True
)

trainer.train()
model.save_pretrained("modelo_finetune_lora")
tokenizer.save_pretrained("modelo_finetune_lora")
print("Modelo entrenado y guardado.")