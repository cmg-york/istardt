% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-05-21 20:12:43 
% From source: Spec 
% Using DTTRanslate 
:-consult("DT-Golog-Iface.pl").
:-style_check(-discontiguous).
:-style_check(-singleton).
:- multifile getRewardMode/1.
:- multifile getRewardModeDTG/1.
:- multifile penalizeDeadlock/1.
:- multifile deadlockPenalty/1.
:- multifile getInfeasiblePenalty/1.
:-dynamic(init/1).



%
% OPTIONS 
% 

getNumRuns(4).





%
% CROSS-RUN ELEMENTS 
% 

transStateStructure([



%
% EXPORTED STATE ELEMENTS 
% 

discreteExportedSet([sourceDomestically_Sat,deliveredInTimeDom_fl,deliveredLateDom_fl,neverDeliveredDom_fl,materialOrdered_Sat]).




%
% ACTION LISTS 
% 

agentActionList([sourceDomestically,sourceFromAbroad,buildInHouse,assignToSpecialists]).

agentAction(sourceDomestically).
agentAction(sourceFromAbroad).
agentAction(buildInHouse).
agentAction(assignToSpecialists).

stochasticActionList([successDeliveredInTimeDom,successDeliveredLateDom,failureDeliveredDom,successDeliveredInTimeFrgn,successInHGood,successSpecGood,successSpecBad]).

stochasticAction(successDeliveredInTimeDom).
stochasticAction(successDeliveredLateDom).
stochasticAction(failureDeliveredDom).
stochasticAction(successDeliveredInTimeFrgn).
stochasticAction(successInHGood).
stochasticAction(successSpecGood).
stochasticAction(successSpecBad).

nondetActions(sourceDomestically,_,[successDeliveredInTimeDom,successDeliveredLateDom,failureDeliveredDom]).
nondetActions(sourceFromAbroad,_,[successDeliveredInTimeFrgn]).
nondetActions(buildInHouse,_,[successInHGood]).
nondetActions(assignToSpecialists,_,[successSpecGood,successSpecBad]).

prob(successDeliveredInTimeDom,0.75,_).
prob(successDeliveredLateDom,0.2,_).
prob(failureDeliveredDom,0.05,_).
prob(successDeliveredInTimeFrgn,1.0,_).
prob(successInHGood,1.0,_).
prob(successSpecGood,0.7,_).
prob(successSpecBad,0.3,_).



%
% PROCEDURES 
% 

proc(productManufactured,materialOrdered : manufacturingCompleted).
proc(materialOrdered,sourceDomestically # sourceFromAbroad).
proc(manufacturingCompleted,buildInHouse # assignToSpecialists).



%
% FLUENT LISTS 
% 

fluentList([deliveredInTimeDom_fl,deliveredLateDom_fl,neverDeliveredDom_fl,deliveredInTimeFrgn_fl,deliveredGoodQualityInH_fl,deliveredGoodQualitySpec_fl,deliveredBadQualitySpec_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

deliveredInTimeDom_fl(do(A,S)) :- deliveredInTimeDom_fl(S); A=successDeliveredInTimeDom.
deliveredLateDom_fl(do(A,S)) :- deliveredLateDom_fl(S); A=successDeliveredLateDom.
neverDeliveredDom_fl(do(A,S)) :- neverDeliveredDom_fl(S); A=failureDeliveredDom.
deliveredInTimeFrgn_fl(do(A,S)) :- deliveredInTimeFrgn_fl(S); A=successDeliveredInTimeFrgn.
deliveredGoodQualityInH_fl(do(A,S)) :- deliveredGoodQualityInH_fl(S); A=successInHGood.
deliveredGoodQualitySpec_fl(do(A,S)) :- deliveredGoodQualitySpec_fl(S); A=successSpecGood.
deliveredBadQualitySpec_fl(do(A,S)) :- deliveredBadQualitySpec_fl(S); A=successSpecBad.

%
% PRECONDITION AXIOMS 
% 

poss(successDeliveredInTimeDom,S).
poss(successDeliveredLateDom,S).
poss(failureDeliveredDom,S).
poss(successDeliveredInTimeFrgn,S).
poss(successInHGood,S).
poss(successSpecGood,S).
poss(successSpecBad,S).

%
% SATISFACTION FORMULAE 
% 

sourceDomestically_Sat(S) :- deliveredInTimeDom_fl(S);deliveredLateDom_fl(S).
sourceFromAbroad_Sat(S) :- deliveredInTimeFrgn_fl(S).
buildInHouse_Sat(S) :- deliveredGoodQualityInH_fl(S).
assignToSpecialists_Sat(S) :- deliveredGoodQualitySpec_fl(S);deliveredBadQualitySpec_fl(S).
productManufactured_Sat(S) :- materialOrdered_Sat(S),manufacturingCompleted_Sat(S).
materialOrdered_Sat(S) :- sourceDomestically_Sat(S);sourceFromAbroad_Sat(S).
manufacturingCompleted_Sat(S) :- buildInHouse_Sat(S);assignToSpecialists_Sat(S).

%
% ATTEMPT FORMULAE 
% 

sourceDomestically_Att(S) :- deliveredInTimeDom_fl(S);deliveredLateDom_fl(S);neverDeliveredDom_fl(S).
sourceFromAbroad_Att(S) :- deliveredInTimeFrgn_fl(S).
buildInHouse_Att(S) :- deliveredGoodQualityInH_fl(S).
assignToSpecialists_Att(S) :- deliveredGoodQualitySpec_fl(S);deliveredBadQualitySpec_fl(S).
productManufactured_Att(S) :- materialOrdered_Sat(S);manufacturingCompleted_Sat(S).
materialOrdered_Att(S) :- sourceDomestically_Sat(S);sourceFromAbroad_Sat(S).
manufacturingCompleted_Att(S) :- buildInHouse_Sat(S);assignToSpecialists_Sat(S).

%
% REWARD FORMULAE 
% 

reward_reputation(V,S) :- V = 10.
reward_financialGain(V,S) :- V = 10.
reward_totalValue(V,S) :- V = 10.

rewardInst(R,S) :- reward_totalValue(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(successDeliveredInTimeDom,successDeliveredInTimeDom_fl).
senseCondition(successDeliveredLateDom,successDeliveredLateDom_fl).
senseCondition(failureDeliveredDom,failureDeliveredDom_fl).
senseCondition(successDeliveredInTimeFrgn,successDeliveredInTimeFrgn_fl).
senseCondition(successInHGood,successInHGood_fl).
senseCondition(successSpecGood,successSpecGood_fl).
senseCondition(successSpecBad,successSpecBad_fl).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(deliveredInTimeDom_fl,S,deliveredInTimeDom_fl(S)).
restoreSitArg(deliveredLateDom_fl,S,deliveredLateDom_fl(S)).
restoreSitArg(neverDeliveredDom_fl,S,neverDeliveredDom_fl(S)).
restoreSitArg(sourceDomestically_Sat,S,sourceDomestically_Sat(S)).
restoreSitArg(sourceDomestically_Att,S,sourceDomestically_Att(S)).
restoreSitArg(deliveredInTimeFrgn_fl,S,deliveredInTimeFrgn_fl(S)).
restoreSitArg(sourceFromAbroad_Sat,S,sourceFromAbroad_Sat(S)).
restoreSitArg(sourceFromAbroad_Att,S,sourceFromAbroad_Att(S)).
restoreSitArg(deliveredGoodQualityInH_fl,S,deliveredGoodQualityInH_fl(S)).
restoreSitArg(buildInHouse_Sat,S,buildInHouse_Sat(S)).
restoreSitArg(buildInHouse_Att,S,buildInHouse_Att(S)).
restoreSitArg(deliveredGoodQualitySpec_fl,S,deliveredGoodQualitySpec_fl(S)).
restoreSitArg(deliveredBadQualitySpec_fl,S,deliveredBadQualitySpec_fl(S)).
restoreSitArg(assignToSpecialists_Sat,S,assignToSpecialists_Sat(S)).
restoreSitArg(assignToSpecialists_Att,S,assignToSpecialists_Att(S)).
restoreSitArg(productManufactured_Sat,S,productManufactured_Sat(S)).
restoreSitArg(productManufactured_Att,S,productManufactured_Att(S)).
restoreSitArg(materialOrdered_Sat,S,materialOrdered_Sat(S)).
restoreSitArg(materialOrdered_Att,S,materialOrdered_Att(S)).
restoreSitArg(manufacturingCompleted_Sat,S,manufacturingCompleted_Sat(S)).
restoreSitArg(manufacturingCompleted_Att,S,manufacturingCompleted_Att(S)).

