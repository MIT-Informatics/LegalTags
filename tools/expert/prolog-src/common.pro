%% common.pro
%% Common relations and rules for the Legal Tags framework



%% Predicate inScope(Leg, Action) holds if legislation Leg regards action Action
%% as in the scope of the legislation. This predicate should be defined
%% by the file(s) that implement legislation Leg.
:- multifile(inScope/2).

%% Predicates permitted(Leg, Action, ConditionSet) and denied(Leg,
%% Action, ConditionSet) hold if legislation Leg permits
%% (respectively, denies) action Action under conditions
%% ConditionSet. These predicates should be defined by the file(s)
%% that implement legislation Leg. For a given action Action and
%% conditions ConditionSet, it is typically for legislation to either
%% permit the action, deny the action, or be silent about whether the
%% action is permitted or denied; if the legislation both permits and
%% denies the action, then either the legislation is inconsistent, or
%% the modeling of the legislation needs to be re-visited.
:- multifile(permitted/3).
:- multifile(denied/3).

%% Actions range over the actions that can be performed on
%% datasets. Currently, this formalization supports the following
%% actions:

%% deposit(DD, DS, R, CS): represents the deposit of dataset DS into
%%                         repository R by data depositor DD, with
%%                         additional conditions CS describing the
%%                         deposit action.
%%
%% accept(R, DS, DD, CS): represents the acceptance of dataset DS by
%%                         repository R in response to the deposit by
%%                         data depositor DD, with additional
%%                         conditions CS describing the accept action.
%%
%% release(R, DS, DU, DD, CS): represents the release of dataset DS
%%                         from repository R to data user DU, with
%%                         additional conditions CS describing the
%%                         release action; the dataset was originally
%%                         deposited by DD.



%% Predicate licenseRequires(Lic, X) holds if license Lic requires
%% X. This predicate can be described by code that describes licenses.
:- multifile(licenseRequires/2).
:- dynamic(licenseRequires/2).

%% Predicate conditionsRequire(CS, X) holds if condition set CS
%% requires X. This predicate holds either if X is in the condition
%% set, or if a license L is in the condition set, and L requires
%% X. In addition, other files may introduce additional predicates
%% that define when a condition set requires some X.
:- multifile(conditionsRequire/2).
:- dynamic(licenseRequires/2).
conditionsRequire(CS, X) :-
    member(license(L), CS),
    licenseRequires(L, X).

conditionsRequire(CS, X) :-
    member(X, CS).


%% Predicate derivedFrom(DS1, DS2, Trans) holds if dataset DS1 is
%% derived from dataset DS2 by transformation Trans.
:- multifile(derivedFrom/3).
:- dynamic(derivedFrom/3).

%% %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Utility functions

% Make sure that list L is at most length N
bounded(L,N) :-
  between(0,N,C),
  length(L,C).

% Extract the condition set from an action
conditionSetForAction(release(_R, _DS, _DU, _DD, CS), CS).
conditionSetForAction(deposit(_DD, _DS, _R, CS), CS).
conditionSetForAction(accept(_R, _DS, _DD, CS), CS).

% Extract the dataset from an action
dataSetForAction(release(R, DS, _DU, DD, _CS), R, DS, DD).
dataSetForAction(deposit(DD, DS, R, _CS), R, DS, DD).
dataSetForAction(accept(R, DS, DD, _CS), R, DS, DD).

