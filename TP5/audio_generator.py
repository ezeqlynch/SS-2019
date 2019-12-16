import math
import numpy as np
from pydub import AudioSegment
from pydub.playback import play

import argparse
from argparse import RawTextHelpFormatter
from moviepy.editor import *



def argumentParser():
  parser = argparse.ArgumentParser(
      description='This program shows data from .\n', formatter_class=RawTextHelpFormatter)

  parser.add_argument(
      '--staticFile',
      help="Path to the static data file.",
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
    print(pressures)
    
    marbleSound = AudioSegment.from_wav("marble.wav")
    audio = AudioSegment.silent(duration=totalTime*1000, frame_rate=44100)
    for i in range(len(timestamps)):
      if(pressures[i]>1):
        audio = audio.overlay(marbleSound + (pressures[i]/1000 -10), position=timestamps[i]*1000)
    audio.export("pressureSound.wav", format="wav")
    play(audio)
    
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

