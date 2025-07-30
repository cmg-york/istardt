% DT-Golog Specification for Model: Spec 
% Date Translated: TEST 
% From source: Spec 
% Using DTTRanslate 
:-style_check(-discontiguous).
:-style_check(-singleton).
:- multifile getRewardMode/1.
:- multifile getRewardModeDTG/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.
:- multifile val/2.
:-dynamic(init/1).



%
% OPTIONS 
% 

getNumRuns(1).
getObsType(continuous).



%
% CROSS-RUN ELEMENTS 
% 

%
% C R O S S   S T A T E 
%
transStateStructure([roomTemperature(_),runningTime(_),hvacOn_fl]).




%
% EXPORTED STATE ELEMENTS 
% 

%
% D I S C R E T E   E X P O R T S
%
discreteExportedSet([]).


%
% C O N T I N U O U S   E X P O R T S
%
continuousExportedSet([[roomTemperature(_),-10.0,100.0], [runningTime(_),0.0,100.0]]).




%
% INITIALIZATIONS 
% 

init([roomTemperature(30),runningTime(0)]).




%
% ACTION LISTS 
% 

agentActionList([sendOnSignal,sendOffSignal]).

agentAction(sendOnSignal).
agentAction(sendOffSignal).

stochasticActionList([signalOnS_Eff,signalOnF_Eff,signalOffS_Eff,signalOffF_Eff]).

stochasticAction(signalOnS_Eff).
stochasticAction(signalOnF_Eff).
stochasticAction(signalOffS_Eff).
stochasticAction(signalOffF_Eff).

nondetActions(sendOnSignal,_,[signalOnS_Eff,signalOnF_Eff]).
nondetActions(sendOffSignal,_,[signalOffS_Eff,signalOffF_Eff]).

prob(signalOnS_Eff,0.95,_).
prob(signalOnF_Eff,0.05,_).
prob(signalOffS_Eff,0.95,_).
prob(signalOffF_Eff,0.05,_).



%
% PROCEDURES 
% 

proc(controlHeater, sendOnSignal # sendOffSignal).
dtgRun :- write('Policy: '), bp(controlHeater,10,_,U,P,x),nl,
        write('Utility: '),writeln(U), 
        write('Probability: '),writeln(P).
dtgRun(L,U,P) :-  with_output_to(string(_),bp(controlHeater,10,L,U,P,x)).



%
% FLUENT LISTS 
% 

fluentList([signalOnS_fl,signalOnF_fl,signalOffS_fl,signalOffF_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

signalOnS_fl(do(A,S)) :- signalOnS_fl(S); A=signalOnS_Eff.
signalOnF_fl(do(A,S)) :- signalOnF_fl(S); A=signalOnF_Eff.
signalOffS_fl(do(A,S)) :- signalOffS_fl(S); A=signalOffS_Eff.
signalOffF_fl(do(A,S)) :- signalOffF_fl(S); A=signalOffF_Eff.

%
% PRECONDITION AXIOMS 
% 

poss(signalOnS_Eff,S) :- \+ sendOnSignal_Att(S),\+ sendOffSignal_Att(S).
poss(signalOnF_Eff,S) :- \+ sendOnSignal_Att(S),\+ sendOffSignal_Att(S).
poss(signalOffS_Eff,S) :- \+ sendOffSignal_Att(S),\+ sendOnSignal_Att(S).
poss(signalOffF_Eff,S) :- \+ sendOffSignal_Att(S),\+ sendOnSignal_Att(S).


poss(sendOnSignal,S) :- (poss(signalOnS_Eff,S);poss(signalOnF_Eff,S)).
poss(sendOffSignal,S) :- (poss(signalOffS_Eff,S);poss(signalOffF_Eff,S)).

%
% SATISFACTION FORMULAE 
% 

sendOnSignal_Sat(S) :- signalOnS_fl(S).
sendOffSignal_Sat(S) :- signalOffS_fl(S).
controlHeater_Sat(S) :- sendOnSignal_Sat(S);sendOffSignal_Sat(S).


% Condition Box Related
hvacOn_fl(s0) :- !,initiallyTrue(hvacOn_fl).
hvacOn_fl(S) :- ((hvacOn_fl(s0),\+ (signalOffS_fl(S)));signalOnS_fl(S)).


% Effect Related
signalOnS_Eff_Sat(S) :- signalOnS_fl(S).
signalOnF_Eff_Sat(S) :- signalOnF_fl(S).
signalOffS_Eff_Sat(S) :- signalOffS_fl(S).
signalOffF_Eff_Sat(S) :- signalOffF_fl(S).


%
% ATTEMPT FORMULAE 
% 

sendOnSignal_Att(S) :- signalOnS_fl(S);signalOnF_fl(S).
sendOffSignal_Att(S) :- signalOffS_fl(S);signalOffF_fl(S).
controlHeater_Att(S) :- sendOnSignal_Att(S);sendOffSignal_Att(S).

%
% ROOT SATISFACTION 
% 

goalAchieved(S) :- controlHeater_Sat(S).

%
% REWARD FORMULAE 
% 

cost(V_init,s0) :- getInitValue(cost,V_init),!.
cost(V,S) :- 
              runningTime(R_runningTime,S),
              V is 
(0.0) - ((0.06) * (R_runningTime)).


comfort(V_init,s0) :- getInitValue(comfort,V_init),!.
comfort(V,S) :- 
                 roomTemperature(R_roomTemperature,S),
                 roomTemperature(R_roomTemperature,S),
                 V is 
(((R_roomTemperature) - (23.0)) * ((R_roomTemperature) - (23.0))) * (-0.1).


overallQuality(V_init,s0) :- getInitValue(overallQuality,V_init),!.
overallQuality(V,S) :- 
                        cost(R_cost,S),
                        comfort(R_comfort,S),
                        V is 
((0.3) * (R_cost)) + ((0.7) * (R_comfort)).


roomTemperature(V_init,s0) :- getInitValue(roomTemperature,V_init),!.
roomTemperature(V,S) :- 
                         roomTemperature(R_roomTemperature,s0),
                         val(R_hvacOn_fl,hvacOn_fl(S)),
                         roomTemperature(R_roomTemperature,s0),
                         V is 
((0.5) + ((0.9) * (R_roomTemperature))) + ((R_hvacOn_fl) * (((0.05) * (R_roomTemperature)) + (1.75))).


runningTime(V_init,s0) :- getInitValue(runningTime,V_init),!.
runningTime(V,S) :- 
                     runningTime(R_runningTime,s0),
                     val(R_hvacOn_fl,hvacOn_fl(S)),
                     V is 
(R_runningTime) + ((10.0) * (R_hvacOn_fl)).



rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(signalOnS_Eff,signalOnS_Eff_Occured).
senseCondition(signalOnF_Eff,signalOnF_Eff_Occured).
senseCondition(signalOffS_Eff,signalOffS_Eff_Occured).
senseCondition(signalOffF_Eff,signalOffF_Eff_Occured).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(signalOnS_fl,S,signalOnS_fl(S)).
restoreSitArg(signalOnF_fl,S,signalOnF_fl(S)).
restoreSitArg(sendOnSignal_Sat,S,sendOnSignal_Sat(S)).
restoreSitArg(sendOnSignal_Att,S,sendOnSignal_Att(S)).
restoreSitArg(signalOffS_fl,S,signalOffS_fl(S)).
restoreSitArg(signalOffF_fl,S,signalOffF_fl(S)).
restoreSitArg(sendOffSignal_Sat,S,sendOffSignal_Sat(S)).
restoreSitArg(sendOffSignal_Att,S,sendOffSignal_Att(S)).
restoreSitArg(controlHeater_Sat,S,controlHeater_Sat(S)).
restoreSitArg(controlHeater_Att,S,controlHeater_Att(S)).
restoreSitArg(cost(X),S,cost(X,S)).
restoreSitArg(comfort(X),S,comfort(X,S)).
restoreSitArg(overallQuality(X),S,overallQuality(X,S)).
restoreSitArg(roomTemperature(X),S,roomTemperature(X,S)).
restoreSitArg(runningTime(X),S,runningTime(X,S)).
restoreSitArg(hvacOn_fl,S,hvacOn_fl(S)).

