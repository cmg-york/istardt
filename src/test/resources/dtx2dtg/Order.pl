% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-07-23 12:00:15 
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
transStateStructure([cost(_),happyCustomer(_)]).




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
% INITIALIZATIONS 
% 

init([happyCustomer(0),cost(0),overallQuality(0)]).




%
% ACTION LISTS 
% 

agentActionList([orderFromSupplierA,orderFromSupplierB]).

agentAction(orderFromSupplierA).
agentAction(orderFromSupplierB).

stochasticActionList([deliveredInTimeA_Eff,neverDeliveredA_Eff,deliveredLateA_Eff,deliveredInTimeB_Eff,neverDeliveredB_Eff,deliveredLateB_Eff]).

stochasticAction(deliveredInTimeA_Eff).
stochasticAction(neverDeliveredA_Eff).
stochasticAction(deliveredLateA_Eff).
stochasticAction(deliveredInTimeB_Eff).
stochasticAction(neverDeliveredB_Eff).
stochasticAction(deliveredLateB_Eff).

nondetActions(orderFromSupplierA,_,[deliveredInTimeA_Eff,neverDeliveredA_Eff,deliveredLateA_Eff]).
nondetActions(orderFromSupplierB,_,[deliveredInTimeB_Eff,neverDeliveredB_Eff,deliveredLateB_Eff]).

prob(deliveredInTimeA_Eff,0.75,_).
prob(neverDeliveredA_Eff,0.05,_).
prob(deliveredLateA_Eff,0.2,_).
prob(deliveredInTimeB_Eff,0.5,_).
prob(neverDeliveredB_Eff,0.15,_).
prob(deliveredLateB_Eff,0.35,_).



%
% PROCEDURES 
% 

proc(orderMaterial, orderFromSupplierA # orderFromSupplierB).
dtgRun :- write('Policy: '), bp(orderMaterial,10,_,U,P,x),nl,
        write('Utility: '),writeln(U), 
        write('Probability: '),writeln(P).


%
% FLUENT LISTS 
% 

fluentList([deliveredInTimeA_fl,neverDeliveredA_fl,deliveredLateA_fl,deliveredInTimeB_fl,neverDeliveredB_fl,deliveredLateB_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

deliveredInTimeA_fl(do(A,S)) :- deliveredInTimeA_fl(S); A=deliveredInTimeA_Eff.
neverDeliveredA_fl(do(A,S)) :- neverDeliveredA_fl(S); A=neverDeliveredA_Eff.
deliveredLateA_fl(do(A,S)) :- deliveredLateA_fl(S); A=deliveredLateA_Eff.
deliveredInTimeB_fl(do(A,S)) :- deliveredInTimeB_fl(S); A=deliveredInTimeB_Eff.
neverDeliveredB_fl(do(A,S)) :- neverDeliveredB_fl(S); A=neverDeliveredB_Eff.
deliveredLateB_fl(do(A,S)) :- deliveredLateB_fl(S); A=deliveredLateB_Eff.

%
% PRECONDITION AXIOMS 
% 

poss(deliveredInTimeA_Eff,S) :- \+ orderFromSupplierA_Att(S),\+ orderFromSupplierB_Att(S).
poss(neverDeliveredA_Eff,S) :- \+ orderFromSupplierA_Att(S),\+ orderFromSupplierB_Att(S).
poss(deliveredLateA_Eff,S) :- \+ orderFromSupplierA_Att(S),\+ orderFromSupplierB_Att(S).
poss(deliveredInTimeB_Eff,S) :- \+ orderFromSupplierB_Att(S),\+ orderFromSupplierA_Att(S).
poss(neverDeliveredB_Eff,S) :- \+ orderFromSupplierB_Att(S),\+ orderFromSupplierA_Att(S).
poss(deliveredLateB_Eff,S) :- \+ orderFromSupplierB_Att(S),\+ orderFromSupplierA_Att(S).


poss(orderFromSupplierA,S) :- (poss(deliveredInTimeA_Eff,S);poss(neverDeliveredA_Eff,S);poss(deliveredLateA_Eff,S)).
poss(orderFromSupplierB,S) :- (poss(deliveredInTimeB_Eff,S);poss(neverDeliveredB_Eff,S);poss(deliveredLateB_Eff,S)).

%
% SATISFACTION FORMULAE 
% 

orderFromSupplierA_Sat(S) :- deliveredInTimeA_fl(S);deliveredLateA_fl(S).
orderFromSupplierB_Sat(S) :- deliveredInTimeB_fl(S);deliveredLateB_fl(S).
orderMaterial_Sat(S) :- orderFromSupplierA_Sat(S);orderFromSupplierB_Sat(S).


% Condition Box Related


% Effect Related
deliveredInTimeA_Eff_Sat(S) :- deliveredInTimeA_fl(S).
neverDeliveredA_Eff_Sat(S) :- neverDeliveredA_fl(S).
deliveredLateA_Eff_Sat(S) :- deliveredLateA_fl(S).
deliveredInTimeB_Eff_Sat(S) :- deliveredInTimeB_fl(S).
neverDeliveredB_Eff_Sat(S) :- neverDeliveredB_fl(S).
deliveredLateB_Eff_Sat(S) :- deliveredLateB_fl(S).


%
% ATTEMPT FORMULAE 
% 

orderFromSupplierA_Att(S) :- deliveredInTimeA_fl(S);neverDeliveredA_fl(S);deliveredLateA_fl(S).
orderFromSupplierB_Att(S) :- deliveredInTimeB_fl(S);neverDeliveredB_fl(S);deliveredLateB_fl(S).
orderMaterial_Att(S) :- orderFromSupplierA_Att(S);orderFromSupplierB_Att(S).

%
% ROOT SATISFACTION 
% 

goalAchieved(S) :- orderMaterial_Sat(S).

%
% REWARD FORMULAE 
% 

cost(V_init,s0) :- getInitValue(cost,V_init),!.
cost(V,S) :-cost(R_cost_init,s0),
              val(R_deliveredInTimeA_fl,deliveredInTimeA_fl(S)),
              val(R_deliveredLateA_fl,deliveredLateA_fl(S)),
              val(R_neverDeliveredA_fl,neverDeliveredA_fl(S)),
              val(R_deliveredInTimeB_fl,deliveredInTimeB_fl(S)),
              val(R_deliveredLateB_fl,deliveredLateB_fl(S)),
              val(R_neverDeliveredB_fl,neverDeliveredB_fl(S)),
              V is R_cost_init +
((((((0.5) * (R_deliveredInTimeA_fl)) + ((0.5) * (R_deliveredLateA_fl))) + ((0.5) * (R_neverDeliveredA_fl))) + ((1.0) * (R_deliveredInTimeB_fl))) + ((1.0) * (R_deliveredLateB_fl))) + ((1.0) * (R_neverDeliveredB_fl)).


happyCustomer(V_init,s0) :- getInitValue(happyCustomer,V_init),!.
happyCustomer(V,S) :-happyCustomer(R_happyCustomer_init,s0),
                       val(R_deliveredInTimeA_fl,deliveredInTimeA_fl(S)),
                       val(R_deliveredLateA_fl,deliveredLateA_fl(S)),
                       val(R_deliveredInTimeB_fl,deliveredInTimeB_fl(S)),
                       val(R_deliveredLateB_fl,deliveredLateB_fl(S)),
                       V is R_happyCustomer_init +
((((1.0) * (R_deliveredInTimeA_fl)) + ((0.7) * (R_deliveredLateA_fl))) + ((1.0) * (R_deliveredInTimeB_fl))) + ((0.7) * (R_deliveredLateB_fl)).


overallQuality(V_init,s0) :- getInitValue(overallQuality,V_init),!.
overallQuality(V,S) :- 
                        cost(R_cost,S),
                        happyCustomer(R_happyCustomer,S),
                        V is 
((0.7) * (R_cost)) + ((0.3) * (R_happyCustomer)).



rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(deliveredInTimeA_Eff,deliveredInTimeA_Eff_Occured).
senseCondition(neverDeliveredA_Eff,neverDeliveredA_Eff_Occured).
senseCondition(deliveredLateA_Eff,deliveredLateA_Eff_Occured).
senseCondition(deliveredInTimeB_Eff,deliveredInTimeB_Eff_Occured).
senseCondition(neverDeliveredB_Eff,neverDeliveredB_Eff_Occured).
senseCondition(deliveredLateB_Eff,deliveredLateB_Eff_Occured).

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
restoreSitArg(cost(X),S,cost(X,S)).
restoreSitArg(happyCustomer(X),S,happyCustomer(X,S)).
restoreSitArg(overallQuality(X),S,overallQuality(X,S)).

