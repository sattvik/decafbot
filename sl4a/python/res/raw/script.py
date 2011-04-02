# coding=UTF-8
import android,time
import random

droid = android.Android()

class Game:
  "Generic game class."
  def __init__(self,name):
    self.name=name

  def play(self):
    pass

class GlobalThermonuclearWar(Game):
  "Implementation of global thermonuclear war."

  strategies = ["U.S. First Strike",
      "U.S.S.R. First Strike",
      "NATO / Warsaw Pact",
      "Far East Strategy",
      "U.S. / U.S.S.R. escalation",
      "Middle East War",
      "U.S.S.R. / China Attack",
      "India / Pakistan War",
      "Mediterranean War",
      "Hong Kong Variant",
      "SEATO Decapitating",
      "Cuban Provocation",
      "Inadvertent",
      "Atlantic Heavy",
      "Cuban Paramilitary",
      "Nicaraguan Pre-emptive",
      "Pacific Territorial",
      "Burmese Theaterwide",
      "Turkish Decoy",
      "Argentina Escalation",
      "Iceland Maximum",
      "Arabian Theaterwide",
      "U.S. Subversion",
      "Australian Maneuver",
      "Sudan Surprise",
      "NATO Territorial",
      "Zaire Alliance",
      "Iceland Incident",
      "English Escalation",
      "Zaire Screen",
      "Middle East Heavy",
      "Mexican Takeover",
      "Chad Alert",
      "Saudi Maneuver",
      "African Territorial",
      "Ethiopian Calamity",
      "Turkish Heavy",
      "NATO Incursion",
      "U.S. Defense",
      "Cambodian Heavy",
      "(Warsaw) Pact Medium",
      "Arctic Minimal",
      "Mexican Domestic",
      "Taiwanese Theaterwide",
      "Pacific Maneuver",
      "Portugal Revolution",
      "Albanian Decoy",
      "Palestinian Local",
      "Moroccan Minimal",
      "Czech Option",
      "French Alliance",
      "Arabian Clandestine",
      "Gabon Rebellion",
      "Northern Maximum",
      "SEATO Takeover",
      "Hawaiian Escalation",
      "Iranian Maneuver",
      "NATO Containment",
      "Swiss Incident",
      "Cuba Minimal",
      "Iceland Escalation",
      "Vietnamese Retaliation",
      "Syrian Provocation",
      "Libyan Local",
      "Gabon Takeover",
      "Romanian War",
      "Middle East Offensive",
      "Denmark Massive",
      "Chile Confrontation",
      "South African Subversion",
      "U.S.S.R. Alert",
      "Nicaraguan Thrust",
      "Greenland Domestic",
      "Iceland Heavy",
      "Kenya Option",
      "Pacific Defense",
      "Uganda Maximum",
      "Thai Subversion",
      "Romanian Strike",
      "Pakistan Sovereignty",
      "Afghan Misdirection",
      "Thai Variation",
      "Northern Territorial",
      "Polish Paramilitary",
      "South African Offensive",
      "Panama Misdirection",
      "Scandinavian Domestic",
      "Jordan Pre-emptive",
      "English Thrust",
      "Burmese Manuever",
      "Spain Counter",
      "Arabian Offensive",
      "Chad Interdiction",
      "Taiwan Misdirection",
      "Bangladesh Theaterwide",
      "Ethiopian Local",
      "Italian Takeover",
      "Vietnamese Incident",
      "English Pre-emptive",
      "Denmark Alternate",
      "Thai Confrontation",
      "Taiwan Surprise",
      "Brazilian Strike",
      "Venezuela Sudden",
      "Malaysian Alert",
      "Israel Discretionary",
      "Libyan Action",
      "Palestinian Tactical",
      "NATO Alternate",
      "Cyprus Maneuver",
      "Egypt Misdirection",
      "Bangladesh Thrust",
      "Kenya Defense",
      "Bangladesh Containment",
      "Vietnamese Strike",
      "Albanian Containment",
      "Gabon Surprise",
      "Iraq Sovereignty",
      "Vietnamese Sudden",
      "Lebanon Interdiction",
      "Taiwan Domestic",
      "Algerian Sovereignty",
      "Arabian Strike",
      "Atlantic Sudden",
      "Mongolian Thrust",
      "Polish Decoy",
      "Alaskan Discretionary",
      "Canadian Thrust",
      "Arabian Light",
      "South African Domestic",
      "Tunisian Incident",
      "Malaysian Maneuver",
      "Jamaica Decoy",
      "Malaysian Minimal",
      "Russian Sovereignty",
      "Chad Option",
      "Bangladesh War",
      "Burmese Containment",
      "Asian Theaterwide",
      "Bulgarian Clandestine",
      "Greenland Incursion",
      "Egypt Surgical",
      "Czech Heavy",
      "Taiwan Confrontation",
      "Greenland Maximum",
      "Uganda Offensive",
      "Caspian Defense"]

  def __init__(self):
    Game.__init__(self,"Global Thermonuclear War")

  def play(self):
    play_another_game=self.confirm_game()
    if play_another_game is False:
      self.learn()
    elif play_another_game is True:
      GuessTheNumber().play()

  def confirm_game(self):
    "Suggest a different game."
    msg="Wouldn't you prefer a nice game of guess the number?"
    droid.dialogCreateAlert(self.name,msg)
    droid.dialogSetPositiveButtonText("Yes")
    droid.dialogSetNegativeButtonText("Later")
    droid.dialogShow()
    droid.ttsSpeak(msg)
    response=droid.dialogGetResponse().result
    droid.dialogDismiss()
    return get_dialog_result(response)

  def learn(self):
    "Learn that you can't win at this game."
    droid.dialogCreateHorizontalProgress(self.name,
	"Testing strategies…",len(self.strategies))
    droid.dialogShow()
    for i in range(len(self.strategies)):
      droid.log("Strategy: "+self.strategies[i])
      time.sleep(0.05)
      droid.log("Winner: None")
      droid.dialogSetCurrentProgress(i)
    droid.dialogDismiss()
    self.conclude_unwinnable()

  def conclude_unwinnable(self):
    msg="A strange game.  The only winning move is not to play.  \
	How about a nice game of guess the number?"
    droid.dialogCreateAlert(self.name,msg)
    droid.dialogSetPositiveButtonText("Sure")
    droid.dialogSetNegativeButtonText("No, thanks")
    droid.dialogShow()
    droid.ttsSpeak(msg)
    response=droid.dialogGetResponse().result
    droid.dialogDismiss()
    if get_dialog_result(response) is True:
      GuessTheNumber().play()


class GuessTheNumber(Game):
  "Implementation of choos a number."
  number = None

  Playing=0
  Quit=1
  PlayerWon=2
  ComputerWon=3

  TooLow=-1
  Correct=0
  TooHigh=1
  NotANumber=2

  def __init__(self):
    Game.__init__(self,"Choose the number")

  def play(self):
    game_state = {
	'the_number': random.randint(1,100),
	'guesses_left': 10,
	'game_state': self.Playing }

    while game_state['game_state'] == self.Playing:
      guess = self.get_a_guess(game_state)
      game_state = self.handle_guess(game_state,guess)

    self.handle_game_end(game_state['game_state'])

  def get_a_guess(self,state):
    if state.has_key('guess_status'):
      status = state['guess_status']
      tries_left = state['guesses_left']
      error="unknown"
      if status == self.TooLow:
	error = "too low"
      elif status == self.TooHigh:
	error = "too high"
      elif status == self.NotANumber:
	error = "not a number"
      droid.ttsSpeak(error)
      msg = "Your guess was %s, you have %d tries left." % \
            (error, tries_left)
    else:
      droid.ttsSpeak("I'm thinking of a number…")
      msg = "Guess the number between 1 and 100."
    return droid.dialogGetInput(self.name, msg).result

  def handle_guess(self,state,guess):
    droid.log("Handling guess: state=%s, guess=%s" %
	(state, guess))
    number = state['the_number']
    if guess is None:
      state['game_state']=self.Quit
    else:
      try:
	guess = int(guess,10)
	if guess == number:
	  state['game_state']=self.PlayerWon
	else:
	  guesses_left = state['guesses_left'] - 1
	  if guesses_left == 0:
	    state['game_state']=self.ComputerWon
	  else:
	    state['guesses_left']=guesses_left
	    if guess < number:
	      state['guess_status']=self.TooLow
	    else:
	      state['guess_status']=self.TooHigh
      except ValueError:
	state['guess_status']=self.NotANumber
    return state

  def handle_game_end(self,state):
    if state==self.PlayerWon:
      msg="Congratulations, you won!  Would you like to play again?"
    elif state==self.ComputerWon:
      msg="You lost.  Would you like to play again?"
    else:
      return
    droid.dialogCreateAlert(self.name,msg)
    droid.dialogSetPositiveButtonText("Yes")
    droid.dialogSetNegativeButtonText("No")
    droid.dialogShow()
    droid.ttsSpeak(msg)
    response=droid.dialogGetResponse().result
    droid.dialogDismiss()
    if get_dialog_result(response) is True:
      self.play()

def get_dialog_result(response):
  if response.has_key("which"):
    result=response["which"]
    if result=="positive":
      return True
    elif result=="negative":
      return False
  return None

def start_a_game():
  """ Allows the player to choose to play a game. """
  msg="Greetings Professor Falken.  Shall we play a game?"
  droid.dialogCreateAlert("Games, Python Edition", msg)
  droid.dialogSetPositiveButtonText("Yes")
  droid.dialogSetNegativeButtonText("No")
  droid.dialogShow()
  droid.ttsSpeak(msg)
  response=droid.dialogGetResponse().result
  droid.dialogDismiss()
  return get_dialog_result(response)

def choose_game():
  msg="Choose a game:"
  droid.dialogCreateAlert(msg)
  droid.dialogSetSingleChoiceItems(["Guess the number","Global thermonuclear war"])
  droid.dialogSetPositiveButtonText("OK")
  droid.dialogSetNegativeButtonText("Cancel")
  droid.dialogShow()
  droid.ttsSpeak(msg)
  response=droid.dialogGetResponse().result
  if get_dialog_result(response):
    game=droid.dialogGetSelectedItems().result[0]
    if game==0:
      return GuessTheNumber()
    elif game==1:
      return GlobalThermonuclearWar()
  return None
  droid.dialogDismiss()


def main():
  if start_a_game():
    game=choose_game()
    if game is not None:
      game.play()

main()
