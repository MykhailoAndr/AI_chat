import requests 
import pygame
import time
import threading

FIFO_PATH = "/tmp/sebastion_fifo2"

def init_mixer():
    pygame.mixer.init()

def play_music(mp3file):
    pygame.mixer.music.load(mp3file)
    pygame.mixer.music.play()
    #while pygame.mixer.music.get_busy():  # Чекаємо, поки музика грає
       # time.sleep(.1)

# Ініціалізуємо мікшер
init_mixer()

# Відтворюємо початкову фразу
initial_text = "Glad to see you, Mr! My name is Sebastion, how can I help you?"
OUTPUT_PATH = "initial_output.mp3"

# Дані для запиту
CHUNK_SIZE = 1024
XI_API_KEY = "sk_eb392b808257fd0e241ef775da507809a9bc9475e55f919a"
VOICE_ID = "N2lVS1w4EtoT3dr4eOWO"
tts_url = f"https://api.elevenlabs.io/v1/text-to-speech/{VOICE_ID}/stream"

# Заголовки
headers = {
    "Accept": "application/json",
    "xi-api-key": XI_API_KEY
}

# Дані
data = {
    "text": initial_text,
    "model_id": "eleven_multilingual_v2",
    "voice_settings": {
        "stability": 0.5,
        "similarity_boost": 0.8,
        "style": 0.0,
        "use_speaker_boost": True
    }
}

# Отримуємо відповідь
response = requests.post(tts_url, headers=headers, json=data, stream=True)

# Перевіряємо, чи запит був успішним
if response.ok:
    # Відкриваємо файл
    with open(OUTPUT_PATH, "wb") as f:
        # Читаємо файл і записуємо як аудіофайл
        for chunk in response.iter_content(chunk_size=CHUNK_SIZE):
            f.write(chunk)
   # print("Initial audio stream saved successfully.")

    # Відтворюємо файл в новому потоці
    music_thread = threading.Thread(target=play_music, args=(OUTPUT_PATH,))
    music_thread.start()
    music_thread.join()  # Чекаємо, поки початкова фраза буде програна

while True:
    with open(FIFO_PATH, 'r') as fifo:
        for line in fifo:
            line = line.strip()
            if line:
               # print(f"Received from Java: {line}")
                
                # Відправляємо отриманий текст в API для озвучення
                TEXT_TO_SPEAK = line  # Використовуємо отримане значення
                OUTPUT_PATH = "output.mp3"

                # Запит на озвучення
                response = requests.post(tts_url, headers=headers, json={**data, "text": TEXT_TO_SPEAK}, stream=True)

                # Перевіряємо, чи запит був успішним
                if response.ok:
                    # Відкриваємо файл
                    with open(OUTPUT_PATH, "wb") as f:
                        # Читаємо файл і записуємо як аудіофайл
                        for chunk in response.iter_content(chunk_size=CHUNK_SIZE):
                            f.write(chunk)
                    #print("Audio stream saved successfully.")

                    # Відтворюємо файл в новому потоці
                    music_thread = threading.Thread(target=play_music, args=(OUTPUT_PATH,))
                    music_thread.start()
                    music_thread.join()  # Чекаємо, поки аудіо закінчиться

                #time.sleep(1)  # Додати затримку перед наступним запитом
