% DT-Golog Specification for Model: Spec 
% Date Translated: 2025-07-10 18:23:53 
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
getObsType(continuous).



%
% CROSS-RUN ELEMENTS 
% 

%
% C R O S S   S T A T E 
%
transStateStructure([cost(_),happyCustomer(_),someVar(_),aTruePredicate_fl,neverDeliveredB_fl]).




%
% EXPORTED STATE ELEMENTS 
% 

%
% D I S C R E T E   E X P O R T S
%
discreteExportedSet([deliveredInTimeB_fl,orderMaterial_Sat,orderFromSupplierA_Sat]).


%
% C O N T I N U O U S   E X P O R T S
%
continuousExportedSet([[cost(_),0.0,100.0], [someVar(_),0.0,200.0], [overallQuality(_),0.0,1.0]]).




%
% INITIALIZATIONS 
% 

init([deliveredLateA_fl,happyCustomer(10),cost(0),someVar_fl(100),overallQuality(0)]).




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
dtgRun :- write('Policy: '), bp(orderMaterial,10,_,U,P,x),
        write('Utility: '),writeln(U), 
        write('Probability: '),writeln(P).


%
% FLUENT LISTS 
% 

fluentList([aTruePredicate_fl,anotherTruepredicate_fl,signalONS_fl,deliveredLateA_fl,deliveredInTimeB_fl,neverDeliveredB_fl,deliveredLateB_fl]).

%
% SUCCESSOR STATE AXIOMS 
% 

aTruePredicate_fl(do(A,S)) :- aTruePredicate_fl(S); A=deliveredInTimeA_Eff.
anotherTruepredicate_fl(do(A,S)) :- anotherTruepredicate_fl(S); A=deliveredInTimeA_Eff.
signalONS_fl(do(A,S)) :- signalONS_fl(S); A=neverDeliveredA_Eff.
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

orderFromSupplierA_Sat(S) :- aTruePredicate_fl(S);anotherTruepredicate_fl(S);deliveredLateA_fl(S).
orderFromSupplierB_Sat(S) :- deliveredInTimeB_fl(S);deliveredLateB_fl(S).
orderMaterial_Sat(S) :- orderFromSupplierA_Sat(S);orderFromSupplierB_Sat(S).


 % Condition Box Related
deliveredInTimeXANDD_fl(s0) :- !,initiallyTrue(deliveredInTimeXANDD_fl).
deliveredInTimeXANDD_fl(S) :- ((hvacOn_fl(S),\+ (signalOffS_fl(S)));signalONS_fl(S)).
deliveredLateAORDeliveredInTimeB_fl(s0) :- !,initiallyTrue(deliveredLateAORDeliveredInTimeB_fl).
deliveredLateAORDeliveredInTimeB_fl(S) :- (deliveredLateA_fl(S);deliveredInTimeB_fl(S)).
deliveredLateB_fl(s0) :- !,initiallyTrue(deliveredLateB_fl).
deliveredLateB_fl(S) :- deliveredLateB_fl(S).
lonely_fl(s0) :- !,initiallyTrue(lonely_fl).
lonely_fl(S) :- (deliveredLateA_fl(s0);neverDeliveredB_fl(S)).

%
% ATTEMPT FORMULAE 
% 

orderFromSupplierA_Att(S) :- aTruePredicate_fl(S);anotherTruepredicate_fl(S);signalONS_fl(S);deliveredLateA_fl(S).
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
parseSimpleQualityExpressionPart1 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1              val(R_deliveredLateA_fl,deliveredLateA_fl(S)),
parseSimpleQualityExpressionPart1 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1              val(R_deliveredInTimeB_fl,deliveredInTimeB_fl(S)),
              val(R_deliveredLateB_fl,deliveredLateB_fl(S)),
              val(R_neverDeliveredB_fl,neverDeliveredB_fl(S)),
              V is R_cost_init +
((((((0.5) * (parseSimpleQualityExpressionPart2 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1)) + ((0.5) * (R_deliveredLateA_fl))) + ((0.5) * (parseSimpleQualityExpressionPart2 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1))) + ((1.0) * (R_deliveredInTimeB_fl))) + ((1.0) * (R_deliveredLateB_fl))) + ((1.0) * (R_neverDeliveredB_fl)).


happyCustomer(V_init,s0) :- getInitValue(happyCustomer,V_init),!.
happyCustomer(V,S) :-happyCustomer(R_happyCustomer_init,s0),
parseSimpleQualityExpressionPart1 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1                       val(R_deliveredLateA_fl,deliveredLateA_fl(S)),
                       val(R_deliveredInTimeB_fl,deliveredInTimeB_fl(S)),
                       val(R_deliveredLateB_fl,deliveredLateB_fl(S)),
                       V is R_happyCustomer_init +
((((1.0) * (parseSimpleQualityExpressionPart2 warning: type of |Unknown PredicateID| is: class ca.yorku.cmg.istardt.xmlparser.objects.Formula$1)) + ((0.7) * (R_deliveredLateA_fl))) + ((1.0) * (R_deliveredInTimeB_fl))) + ((0.7) * (R_deliveredLateB_fl)).


overallQuality(V_init,s0) :- getInitValue(overallQuality,V_init),!.
overallQuality(V,S) :- 
                        cost(R_cost,S),
                        happyCustomer(R_happyCustomer,S),
                        V is 
((0.7) * (R_cost)) + ((0.3) * (R_happyCustomer)).


lonelyQuality(V_init,s0) :- getInitValue(lonelyQuality,V_init),!.
lonelyQuality(V,S) :- 
                       val(R_deliveredLateA_fl,deliveredLateA_fl(S)),
                       val(R_neverDeliveredB_fl,neverDeliveredB_fl(S)),
                       V is 
((0.5) * (R_deliveredLateA_fl)) + ((1.0) * (R_neverDeliveredB_fl)).



rewardInst(R,S) :- overallQuality(R,S).


%
% SENSE CONDITIONS 
% 

senseCondition(deliveredInTimeA_Eff,deliveredInTimeA_Eff_fl).
senseCondition(deliveredInTimeA_Eff,deliveredInTimeA_Eff_fl).
senseCondition(neverDeliveredA_Eff,neverDeliveredA_Eff_fl).
senseCondition(deliveredLateA_Eff,deliveredLateA_Eff_fl).
senseCondition(deliveredInTimeB_Eff,deliveredInTimeB_Eff_fl).
senseCondition(neverDeliveredB_Eff,neverDeliveredB_Eff_fl).
senseCondition(deliveredLateB_Eff,deliveredLateB_Eff_fl).

%
% RESTORE SITUATION ARGUMENT 
% 

restoreSitArg(aTruePredicate_fl,S,aTruePredicate_fl(S)).
restoreSitArg(anotherTruepredicate_fl,S,anotherTruepredicate_fl(S)).
restoreSitArg(signalONS_fl,S,signalONS_fl(S)).
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
restoreSitArg(lonelyQuality(X),S,lonelyQuality(X,S)).
restoreSitArg(deliveredInTimeXANDD_fl,S,deliveredInTimeXANDD_fl(S)).
restoreSitArg(deliveredLateAORDeliveredInTimeB_fl,S,deliveredLateAORDeliveredInTimeB_fl(S)).
restoreSitArg(deliveredLateB_fl,S,deliveredLateB_fl(S)).
restoreSitArg(lonely_fl,S,lonely_fl(S)).

