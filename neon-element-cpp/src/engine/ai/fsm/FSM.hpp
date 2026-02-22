#pragma once

#include "engine/ai/calculations/AiCalculations.hpp"
#include "engine/ai/enums/AiType.hpp"
#include <memory>

namespace neon {

class AiController;

class FSM {
public:
    FSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc);
    virtual ~FSM() = default;

    static std::unique_ptr<FSM> create(Player* aiPlayer, AiCalculations& calc,
                                        GameType& gameType, AiController& aiCon);

    void fetchAction();

protected:
    virtual void easyAiFetchAction() = 0;
    virtual void normalAiFetchAction() = 0;
    virtual void hardAiFetchAction() = 0;

    Player* aiPlayer_;
    AiController& aiCon_;
    float maxHP_;
    PowerupCalculations& puCalc_;
    PlayersCalculations& playerCalc_;
};

class KillsFSM : public FSM {
public:
    using FSM::FSM;

protected:
    void easyAiFetchAction() override;
    void normalAiFetchAction() override;
    void hardAiFetchAction() override;
};

class TimedFSM : public FSM {
public:
    using FSM::FSM;

protected:
    void easyAiFetchAction() override;
    void normalAiFetchAction() override;
    void hardAiFetchAction() override;
};

class HillFSM : public FSM {
public:
    HillFSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc);

protected:
    void easyAiFetchAction() override;
    void normalAiFetchAction() override;
    void hardAiFetchAction() override;

    HillCalculations& hillCalc_;
};

class RegicideFSM : public FSM {
public:
    RegicideFSM(Player* aiPlayer, AiController& aiCon, AiCalculations& calc);

protected:
    void easyAiFetchAction() override;
    void normalAiFetchAction() override;
    void hardAiFetchAction() override;

    RegicideCalculations& regCalc_;
};

} // namespace neon
