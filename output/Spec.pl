% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-05-23 08:16:45 
% From source: Spec 
% Using DTTRanslate 
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

getNumRuns(1).





%
% CROSS-RUN ELEMENTS 
% 

%
% C R O S S   S T A T E 
%
transStateStructure([cost_fl(_),overallQuality_fl(_),happyCustomer_fl(_)]).




%
% EXPORTED STATE ELEMENTS 
% 

%
% D I S C R E T E   E X P O R T S
%
discreteExportedSet([deliveredInTimeA_fl,deliveredInTimeB_fl,deliveredLateA_fl,deliveredLateB_fl,neverDeliveredA_fl,neverDeliveredB_fl]).


%
% C O N T I N U O U S   E X P O R T S
%
continuousExportedSet([]).




%
% ACTION LISTS 
% 

agentActionList([orderFromSupplierA,orderFromSupplierB]).

agentAction(orderFromSupplierA).
agentAction(orderFromSupplierB).

stochasticActionList([deliveredInTimeAEff,neverDeliveredAEff,deliveredLateAEff,deliveredInTimeBEff,neverDeliveredBEff,deliveredLateBEff]).

stochasticAction(deliveredInTimeAEff).
stochasticAction(neverDeliveredAEff).
stochasticAction(deliveredLateAEff).
stochasticAction(deliveredInTimeBEff).
stochasticAction(neverDeliveredBEff).
stochasticAction(deliveredLateBEff).

nondetActions(orderFromSupplierA,_,[deliveredInTimeAEff,neverDeliveredAEff,deliveredLateAEff]).
nondetActions(orderFromSupplierB,_,[deliveredInTimeBEff,neverDeliveredBEff,deliveredLateBEff]).

prob(deliveredInTimeAEff,0.75,_).
prob(neverDeliveredAEff,0.05,_).
prob(deliveredLateAEff,0.2,_).
prob(deliveredInTimeBEff,0.5,_).
prob(neverDeliveredBEff,0.15,_).
prob(deliveredLateBEff,0.35,_).



%
% PROCEDURES 
% 

proc(orderMateri).



%
% FLUENT LISTS 
% 

fluentList([deliveredInTimeA_fl,neverDeliveredA_fl,deliveredLateA_fl,deliveredInTimeB_fl,neverDeliveredB_fl,deliveredLateB_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

deliveredInTimeA_fl(do(A,S)) :- deliveredInTimeA_fl(S); A=deliveredInTimeAEff.
neverDeliveredA_fl(do(A,S)) :- neverDeliveredA_fl(S); A=neverDeliveredAEff.
deliveredLateA_fl(do(A,S)) :- deliveredLateA_fl(S); A=deliveredLateAEff.
deliveredInTimeB_fl(do(A,S)) :- deliveredInTimeB_fl(S); A=deliveredInTimeBEff.
neverDeliveredB_fl(do(A,S)) :- neverDeliveredB_fl(S); A=neverDeliveredBEff.
deliveredLateB_fl(do(A,S)) :- deliveredLateB_fl(S); A=deliveredLateBEff.

%
% PRECONDITION AXIOMS 
% 

poss(deliveredInTimeAEff,S).
poss(neverDeliveredAEff,S).
poss(deliveredLateAEff,S).
poss(deliveredInTimeBEff,S).
poss(neverDeliveredBEff,S).
poss(deliveredLateBEff,S).

%
% SATISFACTION FORMULAE 
% 

orderFromSupplierA_Sat(S) :- deliveredInTimeA_fl(S);deliveredLateA_fl(S).
orderFromSupplierB_Sat(S) :- deliveredInTimeB_fl(S);deliveredLateB_fl(S).
orderMaterial_Sat(S) :-.

%
% ATTEMPT FORMULAE 
% 

orderFromSupplierA_Att(S) :- deliveredInTimeA_fl(S);neverDeliveredA_fl(S);deliveredLateA_fl(S).
orderFromSupplierB_Att(S) :- deliveredInTimeB_fl(S);neverDeliveredB_fl(S);deliveredLateB_fl(S).
orderMaterial_Att(S) :-.

%
% REWARD FORMULAE 
% 

cost(V,S) :- R .

overallQuality(V,S) :- R .

happyCustomer(V,S) :- R .


rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(deliveredInTimeAEff,deliveredInTimeAEff_fl).
senseCondition(neverDeliveredAEff,neverDeliveredAEff_fl).
senseCondition(deliveredLateAEff,deliveredLateAEff_fl).
senseCondition(deliveredInTimeBEff,deliveredInTimeBEff_fl).
senseCondition(neverDeliveredBEff,neverDeliveredBEff_fl).
senseCondition(deliveredLateBEff,deliveredLateBEff_fl).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(deliveredInTimeA_fl,S,deliveredInTimeA_fl(S)).
restoreSitArg(neverDeliveredA_fl,S,neverDeliveredA_fl(S)).
restoreSitArg(deliveredLateA_fl,S,deliveredLateA_fl(S)).
restoreSitArg(orderFromSupplierA_Sat,S,orderFromSupplierA_Sat(S)).
restoreSitArg(orderFromSupplierA_Att,S,orderFromSupplierA_Att(S)).
restoreSitArg(deliveredInTimeB_fl,S,deliveredInTimeB_fl(S)).
restoreSitArg(neverDeliveredB_fl,S,neverDeliveredB_fl(S)).
restoreSitArg(deliveredLateB_fl,S,deliveredLateB_fl(S)).
restoreSitArg(orderFromSupplierB_Sat,S,orderFromSupplierB_Sat(S)).
restoreSitArg(orderFromSupplierB_Att,S,orderFromSupplierB_Att(S)).
restoreSitArg(orderMaterial_Sat,S,orderMaterial_Sat(S)).
restoreSitArg(orderMaterial_Att,S,orderMaterial_Att(S)).

