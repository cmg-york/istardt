% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-07-21 09:28:36 
% From source: Spec 
% Using DTTRanslate 
:-style_check(-discontiguous).
:-style_check(-singleton).
:- multifile getRewardMode/1.
:- multifile getRewardModeDTG/1.
:- multifile penalizeDeadlock/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.
:- multifile val/2.
:-dynamic(init/1).



%
% OPTIONS 
% 

getNumRuns(1).
getObsType(discrete).



%
% CROSS-RUN ELEMENTS 
% 

%
% C R O S S   S T A T E 
%
transStateStructure([avoidMoneyLoss(_),privacy(_),applicationEfficiency(_)]).




%
% EXPORTED STATE ELEMENTS 
% 

discreteExportedSet(X) :- fluentList(X),!.


%
% C O N T I N U O U S   E X P O R T S
%
continuousExportedSet([]).




%
% INITIALIZATIONS 
% 

init([privacy(0)]).




%
% ACTION LISTS 
% 

agentActionList([bookRefundableTickets,bookNonRefundableTickets,committeeAuthorizes,headAuthorizes,fillInPaperForm,fillInOnlineForm]).

agentAction(bookRefundableTickets).
agentAction(bookNonRefundableTickets).
agentAction(committeeAuthorizes).
agentAction(headAuthorizes).
agentAction(fillInPaperForm).
agentAction(fillInOnlineForm).

stochasticActionList([refTixSucc_Eff,refTixFailed_Eff,nonRefTixSucc_Eff,nonRefTixFailed_Eff,cmtGranted_Eff,cmtDenied_Eff,headGranted_Eff,headDenied_Eff,paperSubmitted_Eff,paperLost_Eff,paperSubmittedWithProblems_Eff,onlineSubmitted_Eff,onlineLost_Eff,onlineSubmittedWithProblems_Eff]).

stochasticAction(refTixSucc_Eff).
stochasticAction(refTixFailed_Eff).
stochasticAction(nonRefTixSucc_Eff).
stochasticAction(nonRefTixFailed_Eff).
stochasticAction(cmtGranted_Eff).
stochasticAction(cmtDenied_Eff).
stochasticAction(headGranted_Eff).
stochasticAction(headDenied_Eff).
stochasticAction(paperSubmitted_Eff).
stochasticAction(paperLost_Eff).
stochasticAction(paperSubmittedWithProblems_Eff).
stochasticAction(onlineSubmitted_Eff).
stochasticAction(onlineLost_Eff).
stochasticAction(onlineSubmittedWithProblems_Eff).

nondetActions(bookRefundableTickets,_,[refTixSucc_Eff,refTixFailed_Eff]).
nondetActions(bookNonRefundableTickets,_,[nonRefTixSucc_Eff,nonRefTixFailed_Eff]).
nondetActions(committeeAuthorizes,_,[cmtGranted_Eff,cmtDenied_Eff]).
nondetActions(headAuthorizes,_,[headGranted_Eff,headDenied_Eff]).
nondetActions(fillInPaperForm,_,[paperSubmitted_Eff,paperLost_Eff,paperSubmittedWithProblems_Eff]).
nondetActions(fillInOnlineForm,_,[onlineSubmitted_Eff,onlineLost_Eff,onlineSubmittedWithProblems_Eff]).

prob(refTixSucc_Eff,0.95,_).
prob(refTixFailed_Eff,0.05,_).
prob(nonRefTixSucc_Eff,0.95,_).
prob(nonRefTixFailed_Eff,0.05,_).
prob(cmtGranted_Eff,0.9,_).
prob(cmtDenied_Eff,0.1,_).
prob(headGranted_Eff,0.7,_).
prob(headDenied_Eff,0.3,_).
prob(paperSubmitted_Eff,0.7,_).
prob(paperLost_Eff,0.1,_).
prob(paperSubmittedWithProblems_Eff,0.2,_).
prob(onlineSubmitted_Eff,0.89,_).
prob(onlineLost_Eff,0.01,_).
prob(onlineSubmittedWithProblems_Eff,0.1,_).



%
% PROCEDURES 
% 

proc(travelOrganized, ticketsBooked : authorizationObtained).
proc(ticketsBooked, bookRefundableTickets # bookNonRefundableTickets).
proc(authorizationSigned, committeeAuthorizes # headAuthorizes).
proc(authorizationObtained, authorizationSigned : applicationPrepared).
proc(applicationPrepared, fillInPaperForm # fillInOnlineForm).
dtgRun :- write('Policy: '), bp(travelOrganized,10,_,U,P,x),
        write('Utility: '),writeln(U), 
        write('Probability: '),writeln(P).


%
% FLUENT LISTS 
% 

fluentList([refTixSucc_fl,refTixFailed_fl,nonRefTixSucc_fl,nonRefTixFailed_fl,cmtGranted_fl,cmtDenied_fl,headGranted_fl,headDenied_fl,paperSubmitted_fl,paperLost_fl,paperSubmittedWithProblems_fl,onlineSubmitted_fl,onlineLost_fl,onlineSubmittedWithProblems_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

refTixSucc_fl(do(A,S)) :- refTixSucc_fl(S); A=refTixSucc_Eff.
refTixFailed_fl(do(A,S)) :- refTixFailed_fl(S); A=refTixFailed_Eff.
nonRefTixSucc_fl(do(A,S)) :- nonRefTixSucc_fl(S); A=nonRefTixSucc_Eff.
nonRefTixFailed_fl(do(A,S)) :- nonRefTixFailed_fl(S); A=nonRefTixFailed_Eff.
cmtGranted_fl(do(A,S)) :- cmtGranted_fl(S); A=cmtGranted_Eff.
cmtDenied_fl(do(A,S)) :- cmtDenied_fl(S); A=cmtDenied_Eff.
headGranted_fl(do(A,S)) :- headGranted_fl(S); A=headGranted_Eff.
headDenied_fl(do(A,S)) :- headDenied_fl(S); A=headDenied_Eff.
paperSubmitted_fl(do(A,S)) :- paperSubmitted_fl(S); A=paperSubmitted_Eff.
paperLost_fl(do(A,S)) :- paperLost_fl(S); A=paperLost_Eff.
paperSubmittedWithProblems_fl(do(A,S)) :- paperSubmittedWithProblems_fl(S); A=paperSubmittedWithProblems_Eff.
onlineSubmitted_fl(do(A,S)) :- onlineSubmitted_fl(S); A=onlineSubmitted_Eff.
onlineLost_fl(do(A,S)) :- onlineLost_fl(S); A=onlineLost_Eff.
onlineSubmittedWithProblems_fl(do(A,S)) :- onlineSubmittedWithProblems_fl(S); A=onlineSubmittedWithProblems_Eff.

%
% PRECONDITION AXIOMS 
% 

poss(refTixSucc_Eff,S) :- \+ bookRefundableTickets_Att(S),\+ bookNonRefundableTickets_Att(S).
poss(refTixFailed_Eff,S) :- \+ bookRefundableTickets_Att(S),\+ bookNonRefundableTickets_Att(S).
poss(nonRefTixSucc_Eff,S) :- \+ bookNonRefundableTickets_Att(S),\+ bookRefundableTickets_Att(S).
poss(nonRefTixFailed_Eff,S) :- \+ bookNonRefundableTickets_Att(S),\+ bookRefundableTickets_Att(S).
poss(cmtGranted_Eff,S) :- \+ committeeAuthorizes_Att(S),\+ headAuthorizes_Att(S).
poss(cmtDenied_Eff,S) :- \+ committeeAuthorizes_Att(S),\+ headAuthorizes_Att(S).
poss(headGranted_Eff,S) :- \+ headAuthorizes_Att(S),\+ committeeAuthorizes_Att(S).
poss(headDenied_Eff,S) :- \+ headAuthorizes_Att(S),\+ committeeAuthorizes_Att(S).
poss(paperSubmitted_Eff,S) :- \+ fillInPaperForm_Att(S),\+ fillInOnlineForm_Att(S).
poss(paperLost_Eff,S) :- \+ fillInPaperForm_Att(S),\+ fillInOnlineForm_Att(S).
poss(paperSubmittedWithProblems_Eff,S) :- \+ fillInPaperForm_Att(S),\+ fillInOnlineForm_Att(S).
poss(onlineSubmitted_Eff,S) :- \+ fillInOnlineForm_Att(S),\+ fillInPaperForm_Att(S).
poss(onlineLost_Eff,S) :- \+ fillInOnlineForm_Att(S),\+ fillInPaperForm_Att(S).
poss(onlineSubmittedWithProblems_Eff,S) :- \+ fillInOnlineForm_Att(S),\+ fillInPaperForm_Att(S).


poss(bookRefundableTickets,S) :- (poss(refTixSucc_Eff,S);poss(refTixFailed_Eff,S)).
poss(bookNonRefundableTickets,S) :- (poss(nonRefTixSucc_Eff,S);poss(nonRefTixFailed_Eff,S)).
poss(committeeAuthorizes,S) :- (poss(cmtGranted_Eff,S);poss(cmtDenied_Eff,S)).
poss(headAuthorizes,S) :- (poss(headGranted_Eff,S);poss(headDenied_Eff,S)).
poss(fillInPaperForm,S) :- (poss(paperSubmitted_Eff,S);poss(paperLost_Eff,S);poss(paperSubmittedWithProblems_Eff,S)).
poss(fillInOnlineForm,S) :- (poss(onlineSubmitted_Eff,S);poss(onlineLost_Eff,S);poss(onlineSubmittedWithProblems_Eff,S)).

%
% SATISFACTION FORMULAE 
% 

bookRefundableTickets_Sat(S) :- refTixSucc_fl(S).
bookNonRefundableTickets_Sat(S) :- nonRefTixSucc_fl(S).
committeeAuthorizes_Sat(S) :- cmtGranted_fl(S).
headAuthorizes_Sat(S) :- headGranted_fl(S).
fillInPaperForm_Sat(S) :- paperSubmitted_fl(S);paperSubmittedWithProblems_fl(S).
fillInOnlineForm_Sat(S) :- onlineSubmitted_fl(S);onlineSubmittedWithProblems_fl(S).
travelOrganized_Sat(S) :- ticketsBooked_Sat(S),authorizationObtained_Sat(S).
ticketsBooked_Sat(S) :- bookRefundableTickets_Sat(S);bookNonRefundableTickets_Sat(S).
authorizationSigned_Sat(S) :- committeeAuthorizes_Sat(S);headAuthorizes_Sat(S).
authorizationObtained_Sat(S) :- authorizationSigned_Sat(S),applicationPrepared_Sat(S).
applicationPrepared_Sat(S) :- fillInPaperForm_Sat(S);fillInOnlineForm_Sat(S).


 % Condition Box Related

%
% ATTEMPT FORMULAE 
% 

bookRefundableTickets_Att(S) :- refTixSucc_fl(S);refTixFailed_fl(S).
bookNonRefundableTickets_Att(S) :- nonRefTixSucc_fl(S);nonRefTixFailed_fl(S).
committeeAuthorizes_Att(S) :- cmtGranted_fl(S);cmtDenied_fl(S).
headAuthorizes_Att(S) :- headGranted_fl(S);headDenied_fl(S).
fillInPaperForm_Att(S) :- paperSubmitted_fl(S);paperLost_fl(S);paperSubmittedWithProblems_fl(S).
fillInOnlineForm_Att(S) :- onlineSubmitted_fl(S);onlineLost_fl(S);onlineSubmittedWithProblems_fl(S).
travelOrganized_Att(S) :- ticketsBooked_Att(S);authorizationObtained_Att(S).
ticketsBooked_Att(S) :- bookRefundableTickets_Att(S);bookNonRefundableTickets_Att(S).
authorizationSigned_Att(S) :- committeeAuthorizes_Att(S);headAuthorizes_Att(S).
authorizationObtained_Att(S) :- authorizationSigned_Att(S);applicationPrepared_Att(S).
applicationPrepared_Att(S) :- fillInPaperForm_Att(S);fillInOnlineForm_Att(S).

%
% ROOT SATISFACTION 
% 

goalAchieved(S) :- travelOrganized_Sat(S).

%
% REWARD FORMULAE 
% 

avoidMoneyLoss(V_init,s0) :- getInitValue(avoidMoneyLoss,V_init),!.
avoidMoneyLoss(V,S) :-avoidMoneyLoss(R_avoidMoneyLoss_init,s0),
                        val(R_refTixSucc_fl,refTixSucc_fl(S)),
                        V is R_avoidMoneyLoss_init +
(1.0) * (R_refTixSucc_fl).


privacy(V_init,s0) :- getInitValue(privacy,V_init),!.
privacy(V,S) :-privacy(R_privacy_init,s0),
                 val(R_headAuthorizes_Sat,headAuthorizes_Sat(S)),
                 V is R_privacy_init +
(0.8) * (R_headAuthorizes_Sat).


overallQuality(V_init,s0) :- getInitValue(overallQuality,V_init),!.
overallQuality(V,S) :- 
                        avoidMoneyLoss(R_avoidMoneyLoss,S),
                        privacy(R_privacy,S),
                        applicationEfficiency(R_applicationEfficiency,S),
                        V is 
(((0.7) * (R_avoidMoneyLoss)) + ((0.3) * (R_privacy))) + ((0.1) * (R_applicationEfficiency)).


applicationEfficiency(V_init,s0) :- getInitValue(applicationEfficiency,V_init),!.
applicationEfficiency(V,S) :-applicationEfficiency(R_applicationEfficiency_init,s0),
                               val(R_paperSubmitted_fl,paperSubmitted_fl(S)),
                               val(R_paperSubmittedWithProblems_fl,paperSubmittedWithProblems_fl(S)),
                               val(R_onlineSubmitted_fl,onlineSubmitted_fl(S)),
                               val(R_onlineSubmittedWithProblems_fl,onlineSubmittedWithProblems_fl(S)),
                               V is R_applicationEfficiency_init +
((((0.7) * (R_paperSubmitted_fl)) + ((0.4) * (R_paperSubmittedWithProblems_fl))) + ((0.1) * (R_onlineSubmitted_fl))) + ((0.1) * (R_onlineSubmittedWithProblems_fl)).



rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(refTixSucc_Eff,refTixSucc_Eff_fl).
senseCondition(refTixFailed_Eff,refTixFailed_Eff_fl).
senseCondition(nonRefTixSucc_Eff,nonRefTixSucc_Eff_fl).
senseCondition(nonRefTixFailed_Eff,nonRefTixFailed_Eff_fl).
senseCondition(cmtGranted_Eff,cmtGranted_Eff_fl).
senseCondition(cmtDenied_Eff,cmtDenied_Eff_fl).
senseCondition(headGranted_Eff,headGranted_Eff_fl).
senseCondition(headDenied_Eff,headDenied_Eff_fl).
senseCondition(paperSubmitted_Eff,paperSubmitted_Eff_fl).
senseCondition(paperLost_Eff,paperLost_Eff_fl).
senseCondition(paperSubmittedWithProblems_Eff,paperSubmittedWithProblems_Eff_fl).
senseCondition(onlineSubmitted_Eff,onlineSubmitted_Eff_fl).
senseCondition(onlineLost_Eff,onlineLost_Eff_fl).
senseCondition(onlineSubmittedWithProblems_Eff,onlineSubmittedWithProblems_Eff_fl).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(refTixSucc_fl,S,refTixSucc_fl(S)).
restoreSitArg(refTixFailed_fl,S,refTixFailed_fl(S)).
restoreSitArg(bookRefundableTickets_Sat,S,bookRefundableTickets_Sat(S)).
restoreSitArg(bookRefundableTickets_Att,S,bookRefundableTickets_Att(S)).
restoreSitArg(nonRefTixSucc_fl,S,nonRefTixSucc_fl(S)).
restoreSitArg(nonRefTixFailed_fl,S,nonRefTixFailed_fl(S)).
restoreSitArg(bookNonRefundableTickets_Sat,S,bookNonRefundableTickets_Sat(S)).
restoreSitArg(bookNonRefundableTickets_Att,S,bookNonRefundableTickets_Att(S)).
restoreSitArg(cmtGranted_fl,S,cmtGranted_fl(S)).
restoreSitArg(cmtDenied_fl,S,cmtDenied_fl(S)).
restoreSitArg(committeeAuthorizes_Sat,S,committeeAuthorizes_Sat(S)).
restoreSitArg(committeeAuthorizes_Att,S,committeeAuthorizes_Att(S)).
restoreSitArg(headGranted_fl,S,headGranted_fl(S)).
restoreSitArg(headDenied_fl,S,headDenied_fl(S)).
restoreSitArg(headAuthorizes_Sat,S,headAuthorizes_Sat(S)).
restoreSitArg(headAuthorizes_Att,S,headAuthorizes_Att(S)).
restoreSitArg(paperSubmitted_fl,S,paperSubmitted_fl(S)).
restoreSitArg(paperLost_fl,S,paperLost_fl(S)).
restoreSitArg(paperSubmittedWithProblems_fl,S,paperSubmittedWithProblems_fl(S)).
restoreSitArg(fillInPaperForm_Sat,S,fillInPaperForm_Sat(S)).
restoreSitArg(fillInPaperForm_Att,S,fillInPaperForm_Att(S)).
restoreSitArg(onlineSubmitted_fl,S,onlineSubmitted_fl(S)).
restoreSitArg(onlineLost_fl,S,onlineLost_fl(S)).
restoreSitArg(onlineSubmittedWithProblems_fl,S,onlineSubmittedWithProblems_fl(S)).
restoreSitArg(fillInOnlineForm_Sat,S,fillInOnlineForm_Sat(S)).
restoreSitArg(fillInOnlineForm_Att,S,fillInOnlineForm_Att(S)).
restoreSitArg(travelOrganized_Sat,S,travelOrganized_Sat(S)).
restoreSitArg(travelOrganized_Att,S,travelOrganized_Att(S)).
restoreSitArg(ticketsBooked_Sat,S,ticketsBooked_Sat(S)).
restoreSitArg(ticketsBooked_Att,S,ticketsBooked_Att(S)).
restoreSitArg(authorizationSigned_Sat,S,authorizationSigned_Sat(S)).
restoreSitArg(authorizationSigned_Att,S,authorizationSigned_Att(S)).
restoreSitArg(authorizationObtained_Sat,S,authorizationObtained_Sat(S)).
restoreSitArg(authorizationObtained_Att,S,authorizationObtained_Att(S)).
restoreSitArg(applicationPrepared_Sat,S,applicationPrepared_Sat(S)).
restoreSitArg(applicationPrepared_Att,S,applicationPrepared_Att(S)).
restoreSitArg(avoidMoneyLoss(X),S,avoidMoneyLoss(X,S)).
restoreSitArg(privacy(X),S,privacy(X,S)).
restoreSitArg(overallQuality(X),S,overallQuality(X,S)).
restoreSitArg(applicationEfficiency(X),S,applicationEfficiency(X,S)).

