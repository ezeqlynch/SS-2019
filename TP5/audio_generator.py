import math
import numpy as np
from pydub import AudioSegment
from pydub.playback import play

import argparse
from argparse import RawTextHelpFormatter
import moviepy.editor as mpe



def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
      default='data/cut.xyz'
  )
  parser.add_argument(
      '--video',
      help="Path to the video file.",
      default='data/cut.xyz'
  )

  return parser


if __name__ == "__main__":
    # get parser
    parsedArgs = argumentParser().parse_args()
    staticFile = open(parsedArgs.staticFile, "r")
    timestamps = []
    pressures = []
    totalTime = 0
    staticFile.readline()
    flag = False
    for line in staticFile:
      if(flag):
        totalTime += 1/60
        timestamps.append(totalTime)
        pressures.append(float(line))
      flag = not flag
    print(totalTime)
    # print(pressures)
    
    marbleSound = AudioSegment.from_wav("marble.wav")
    audio = AudioSegment.silent(duration=totalTime*1000, frame_rate=44100)
    for i in range(len(timestamps)):
      if(pressures[i]>1):
        audio = audio.overlay(marbleSound + (pressures[i]/1000 -30), position=timestamps[i]*1000)
    audio.export("pressureSound.wav", format="wav")
    # play(audio)

    video = mpe.VideoFileClip(parsedArgs.video)
    audio = mpe.AudioFileClip("pressureSound.wav")
    video = video.set_audio(audio.set_duration(video.duration))
    video.write_videofile(parsedArgs.video[:-4] + "-sounded.avi", fps=60, codec='libx264', audio_codec='libvorbis')

    # my_clip = mpe.VideoFileClip(parsedArgs.video)
    # audio_background = mpe.AudioFileClip('pressureSound.wav')
    # # final_audio = mpe.CompositeAudioClip([my_clip.audio, audio_background])
    # final_clip = my_clip.set_audio(audio_background)
    # final_clip.write_videofile("test2.avi", codec='png')
    
    
    # marbleSound = AudioSegment.from_wav("marble.wav")
    # audio = AudioSegment.silent(duration=3000, frame_rate=44100)
    # audio = audio.overlay(marbleSound, position=0)
    # audio.export("mashup.wav", format="wav")
    # play(audio)

# rate = 44100  # samples per second
# T = 3         # sample duration (seconds)
# f = 440.0     # sound frequency (Hz)
# t = np.linspace(0, T, T*rate, endpoint=False)
# # x = np.sin(2*np.pi * f * t)
# # x = np.random.uniform(-1, 1, len(t))
# x = np.linspace(0, 0, T*rate, endpoint=False)
# print(t.shape)
# print(x.shape)
# wavio.write("sine24.wav", x, rate, sampwidth=3)
# playsound('sine24.wav')

