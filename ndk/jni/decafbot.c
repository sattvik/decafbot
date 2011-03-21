#include<jni.h>
#include<stdlib.h>
#include<unistd.h>
#include<android/log.h>
#include "decafbot_jni_GuessTheNumberActivity.h"


#define LOG_TAG "DecafBotJNI"
#define LOGE(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

static const jsize PROGRESS_SIZE = 3;
static const jsize STRATEGY = 0;
static const jsize STRATEGY_NUM = 1;
static const jsize NUM_STRATEGIES = 2;

jobject toInteger(JNIEnv* env, jint anInt) {
  static jclass integerClass;
  static jmethodID valueOfMethod;

  /* get class for Integer */
  if(integerClass == NULL) {
    integerClass = (*env)->FindClass(env, "java/lang/Integer");
    if (integerClass == NULL) {
      LOGE("Failed to find class for Integer");
      return NULL;
    }
  }

  /* get Integer.valueOf(int) method */
  if(valueOfMethod == NULL) {
    valueOfMethod = (*env)->GetStaticMethodID(env, integerClass, "valueOf",
	"(I)Ljava/lang/Integer;");
    if (valueOfMethod == NULL) {
      LOGE("Failed to find static method Integer.valueOf(int)");
      return NULL;
    }
  }

  /* do the conversion */
  return (*env)->CallStaticObjectMethod(env, integerClass, valueOfMethod,
      anInt);
}


/*
 * Class:     decafbot_jni_GlobalThermonuclearWarActivity_LearnTask
 * Method:    doInBackground
 * Signature: ([Ljava/lang/String;)Ljava/lang/Void;
 */
JNIEXPORT jobject JNICALL Java_decafbot_jni_GlobalThermonuclearWarActivity_00024LearnTask_doInBackground
(JNIEnv * env, jobject task, jobjectArray strategies) {
  jsize i;
  jsize numStrategies = (*env)->GetArrayLength(env, strategies);
  jclass taskClass = (*env)->GetObjectClass(env, task);
  jmethodID publishMethod;
  jclass objectArrayClass;
  jobject numStrategiesObj;

  /* get publishProgress method */
  publishMethod = (*env)->GetMethodID(env, taskClass, "publishProgress",
      "([Ljava/lang/Object;)V");
  if(publishMethod==NULL) {
    LOGE("Failed to find publishProgress method");
    return NULL;
  }

  /* get class for Object[] */
  objectArrayClass = (*env)->FindClass(env, "[Ljava/lang/Object;");
  if (objectArrayClass == NULL) {
    LOGE("Failed to find class for Object[]");
    return NULL;
  }

  /* Turn the number of strategies into an object */
  numStrategiesObj = toInteger(env, numStrategies);
  if(numStrategiesObj == NULL) {
    return NULL;
  }

  /* loop through all the strategies. */
  for(i=0; i<numStrategies; ++i) {
    jstring strategy = (*env)->GetObjectArrayElement(env, strategies, i);
    jobjectArray progressData;
    jobject strategyNum;

    /* Think some. */
    usleep(50000);
    
    /* allocate array */
    progressData = (*env)->NewObjectArray(env, PROGRESS_SIZE,
					  objectArrayClass, NULL);
    if (progressData == NULL) {
      LOGE("Failed to allocate object array for published progress.");
      return NULL;
    }

    /* turn strategy number into an Integer object */
    strategyNum = toInteger(env, i);
    if(strategyNum == NULL) {
      return NULL;
    }

    (*env)->SetObjectArrayElement(env, progressData, STRATEGY, strategy);
    (*env)->SetObjectArrayElement(env, progressData, STRATEGY_NUM,
	strategyNum);
    (*env)->SetObjectArrayElement(env, progressData, NUM_STRATEGIES,
	numStrategiesObj);

    /* publish progress */
    (*env)->CallVoidMethod(env, task, publishMethod, progressData);
  }
  return NULL;
}

/******************************/
/***   GUESS THE NUMBER    ****/
/******************************/

static jint guessesLeft;
static jint theNumber;

/*
 * Class:     decafbot_jni_GuessTheNumberActivity
 * Method:    nativeInitGame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_decafbot_jni_GuessTheNumberActivity_nativeInitGame
(JNIEnv *env, jobject activity, jint number) {
  guessesLeft = 10L;
  theNumber =  number;
}


/*
 * Class:     decafbot_jni_GuessTheNumberActivity
 * Method:    analyzeGuess
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_decafbot_jni_GuessTheNumberActivity_analyzeGuess
(JNIEnv* env, jobject activity, jint guess) {
  if(guess == theNumber) {
    return decafbot_jni_GuessTheNumberActivity_PLAYER_WON;
  }
  guessesLeft -= 1;
  if (guessesLeft <= 0) {
    return decafbot_jni_GuessTheNumberActivity_PLAYER_LOST;
  }
  if (guess < theNumber) {
    return decafbot_jni_GuessTheNumberActivity_TOO_LOW;
  } else {
    return decafbot_jni_GuessTheNumberActivity_TOO_HIGH;
  }
}

/*
 * Class:     decafbot_jni_GuessTheNumberActivity
 * Method:    guessesLeft
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_decafbot_jni_GuessTheNumberActivity_guessesLeft
(JNIEnv* env, jobject activity) {
  return guessesLeft;
}
