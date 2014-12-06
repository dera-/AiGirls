package com.aigirls.model.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.manager.ScreenManager;
import com.aigirls.param.ScreenEnum;
import com.aigirls.service.battle.DamageCalculateService;

public class EnemyBrainModel {
    private static final int DEFAULT_DEPTH = 2;
    private static final int WIN_SCORE = 5000;  //高めに設定しておく
    private static int[] ballHeightPenalty = {0, 0, 0, 2, 5, 14, 40, 120};
    private static int[] decreaseHpPenalty = {0, 2, 20, 70, 300, 1000};
    private static int[] ballStackScores = {-80, -30, 0, 15, 30, 60};
    private static int[] FutureBeatScores = {1000, 800, 400, 150, 50};
    private static int IMPPOSIBLE_BEAT = 1000;
    private final int depth;
    private int[][] oneBalls = {{0}, {1}, {2}, {3}, {4}, {5}, {6}};
    private int[][] twoBalls =
        {
            {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6},
            {1,1}, {1,2}, {1,3}, {1,4}, {1,5}, {1,6},
            {2,2}, {2,3}, {2,4}, {2,5}, {2,6},
            {3,3}, {3,4}, {3,5}, {3,6},
            {4,4}, {4,5}, {4,6},
            {5,5}, {5,6},
            {6,6}
        };


    public EnemyBrainModel()
    {
        this(DEFAULT_DEPTH);
    }

    public EnemyBrainModel(int d)
    {
        depth = d;
    }

    public EnemyActionModel decideAction(CharacterModel ai, CharacterModel player)
    {
        return minMax(ai.getClone(), player.getClone(), depth);
    }

    private EnemyActionModel minMax(CharacterModel ai, CharacterModel player, int depth)
    {
        boolean first = (this.depth-depth)%2 == 0;
        if (depth == 0) {
            return new EnemyActionModel(new int[0], null, new BallInfoModel[0], getScore(ai, player, first));
        }
        EnemyActionModel currentAction = new EnemyActionModel(new int[0], null, new BallInfoModel[0], getScore(ai, player, first));
        CharacterModel[] charas = getCharacterModels(ai, player, first);
        //スタックへのボール追加
        charas[0].addBallToStack(1);
        int ballNumToUse = charas[0].getStackedBallNum() - GameConfig.DEFAULT_STACKED_BALL_NUM;
        int[] ballHeights = getBallHeights(charas[0]);
        for (int i=0; i<oneBalls.length ; i++) {
            if (ballNumToUse<oneBalls[i].length || !isPutBalls(oneBalls[i], ballHeights)) {
                continue;
            }
            CharacterModel aiClone = ai.getClone();
            CharacterModel playerClone = player.getClone();
            CharacterModel[] charaClones = getCharacterModels(aiClone, playerClone, first);
            simulatePutBall(charaClones[0], charaClones[1], oneBalls[i]);
            //いろいろきついので一番強い技だけを出すようにする
            ActiveMagicModel magicToUse = getBestMagic(charaClones[0]);
            int length = magicToUse.getNumTargetBalls();
            currentAction = getBetterAction(first, oneBalls[i], null, new BallInfoModel[0], minMax(aiClone, playerClone, depth-1), currentAction);
            for (int index = 0 ; index < length; index++) {
                CharacterModel aiCloneClone = aiClone.getClone();
                CharacterModel playerCloneClone = playerClone.getClone();
                CharacterModel[] charaCloneClones = getCharacterModels(aiCloneClone, playerCloneClone, first);
                magicToUse.getBallInfoModels(index);
                BallInfoModel[] targetBalls = magicToUse.getBallInfoModels(index);
                getScoreAfterAction(charaCloneClones[0], charaCloneClones[1], magicToUse, targetBalls);
                currentAction = getBetterAction(first, oneBalls[i], magicToUse, targetBalls, minMax(aiCloneClone, playerCloneClone, depth-1), currentAction);
            }
        }
        return currentAction;
    }

    private EnemyActionModel getBetterAction(boolean attack, int[] puts, ActiveMagicModel magic, BallInfoModel[] balls, EnemyActionModel futureAction, EnemyActionModel currentAction)
    {
        EnemyActionModel action = new EnemyActionModel(puts, magic, balls, futureAction.score);
        if (attack && currentAction.score < action.score) {
            currentAction = action;
        } else if (!attack && currentAction.score > action.score) {
            currentAction = action;
        }
        return currentAction;
    }

    private ActiveMagicModel getBestMagic(CharacterModel chara){
        ActiveMagicModel magicToUse = null;

        ActiveMagicModel[] magics = chara.getActiveMagicModels();
        for (ActiveMagicModel m : magics) {
            if (m.canOutbreak() && (magicToUse == null || m.getStrength() > magicToUse.getStrength())) {
                magicToUse = m;
            }
        }
        return magicToUse;
    }

    private CharacterModel[] getCharacterModels(CharacterModel ai, CharacterModel player, boolean first)
    {
        CharacterModel[] models = new CharacterModel[2];
        if (first) {
            models[0] = ai;
            models[1] = player;
        } else {
            models[0] = player;
            models[1] = ai;
        }
        return models;
    }

    private int[] getBallHeights(CharacterModel chara)
    {
        int[] heights = new int[GameConfig.BOARD_WIDTH];
        for (int x=0; x<heights.length; x++) {
            int dropPlace = chara.getDropPlace(x);
            if (dropPlace == BoardModel.CAN_NOT_SET_BALL) {
                dropPlace = GameConfig.BOARD_HEIGHT;
            }
            heights[x] = dropPlace;
        }
        return heights;
    }

    private boolean isPutBalls(int[] puts, int[] ballHeights)
    {
        int[] heights = new int[ballHeights.length];
        for (int i=0; i<heights.length; i++) {
            heights[i] = ballHeights[i];
        }
        for (int p : puts) {
            heights[p]++;
            if (heights[p] >= GameConfig.BOARD_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    private int getScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        if (!player.isAlive()) {
            return WIN_SCORE;
        }
        if (!ai.isAlive()) {
            return -1*WIN_SCORE;
        }
        int scoreForAI = calculateBoardScore(ai) + calculateHpScore(ai) + calculateBallStackScore(ai);
        int scoreForPlayer = -1*calculateBoardScore(player) - calculateHpScore(player) - calculateBallStackScore(player);
        int futureScore = calculateFutureScore(ai, player, first); //TODO 本当に使うかどうか速度と相談
        return scoreForAI + scoreForPlayer + futureScore;
    }


    /**
     * ボードの評価値を返す
     * @param chara
     * @return
     */
    private int calculateBoardScore(CharacterModel chara)
    {
        int total = 0;
        for (int x = 0; x < GameConfig.BOARD_WIDTH; x++) {
            int count = chara.getDropPlace(x);
            if (count == BoardModel.CAN_NOT_SET_BALL) {
                count = GameConfig.BOARD_HEIGHT;
            }
            total += ballHeightPenalty[count];
        }
        return -1*total;
    }

    /**
     * 残りHPのに関する評価値を返す
     * @param chara
     * @return
     */
    private int calculateHpScore(CharacterModel chara)
    {
        double rate = 1 - 1.0*chara.getCurrentHp()/chara.getMaxHp();
        if (rate >= 1) {
            return decreaseHpPenalty[decreaseHpPenalty.length-1];
        }
        int index = (int) Math.floor((decreaseHpPenalty.length-1)*rate);
        int minPenalty = decreaseHpPenalty[index];
        int maxPenalty = decreaseHpPenalty[index+1];
        return (int) Math.round(-1.0 * ((maxPenalty-minPenalty) * ((decreaseHpPenalty.length-1)*rate-index) + minPenalty));
    }

    /**
     * ボールスタック数に関する評価値を返す
     *
     */
    private int calculateBallStackScore(CharacterModel chara)
    {
        return ballStackScores[chara.getStackedBallNum()];
    }

    /**
     * 未来で確実に負ける(勝つ)かどうかを調べて、結果を評価値として取得
     * どちらもこのターンで死んでないことが前提
     */
    private int calculateFutureScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        //AI側
        int turnForAi = getTurnNumUntilBeating(ai, player);
        //プレイヤー側
        int turnForPlayer = getTurnNumUntilBeating(player, ai);

        if (turnForAi < turnForPlayer) {
            return FutureBeatScores[turnForAi-1];
        } else if (turnForPlayer < turnForAi) {
            return -1*FutureBeatScores[turnForPlayer-1];
        } else if (turnForAi != IMPPOSIBLE_BEAT) {
            return first ? FutureBeatScores[turnForAi-1] : -1*FutureBeatScores[turnForPlayer-1];
        }
        return 0;
    }

    /**
     * 相手を確実に倒すまでにかかるターン数を返す
     * @param chara
     * @return
     */
    private int getTurnNumUntilBeating(CharacterModel attacker, CharacterModel defender)
    {
        int currentStack = attacker.getStackedBallNum();
        ActiveMagicModel[] magics = attacker.getActiveMagicModels();
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.addBallToStack(1); //スタックにボールの追加(後で必ず消すこと)
            for (ActiveMagicModel magic : magics) {
                if (!magic.canOutbreak()) {
                    continue;
                }
                int damege = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
                if (damege >= defender.getDefense()) {
                    return i;
                }
            }
        }
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.removeBallFromStack();
        }
        return IMPPOSIBLE_BEAT;
    }

    /**
     * ボール配置のシミュレーション
     */
    private void simulatePutBall(CharacterModel attacker, CharacterModel defender, int[] puts)
    {
        for (int x : puts) {
            attacker.removeBallFromStack();
            attacker.setBall(x, new BallModel(0));
            int defenserYPlace = defender.getDropPlace(x);
            if (defenserYPlace == BoardModel.CAN_NOT_SET_BALL) {
                int damage = DamageCalculateService.getDamageValue(attacker.getAttack(), defender.getDefense());
                defender.beHurt(damage);
            } else {
                defender.setBall(x, new ObstacleBallModel(0, attacker.getMagicDefense()));
            }
        }
    }

    /**
     * 攻撃時のシミュレーション
     * @param attacker
     * @param defender
     * @param puts
     */
    private void getScoreAfterAction(CharacterModel attacker, CharacterModel defender, ActiveMagicModel magic, BallInfoModel[] targetBalls)
    {
        int damage = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
        int damageToBall = (int) Math.round(magic.getBallAttackRate() * attacker.getMagicAttack());
        attacker.outbreakMagic(damageToBall, targetBalls);
        defender.beHurt(damage);
        attacker.addBallToStack(magic.getRecoverBall());
    }

}
