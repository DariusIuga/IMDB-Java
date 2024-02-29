package org.example;

public interface ExperienceStrategy{
    public int calculateExperience();
}

class RatingGain implements ExperienceStrategy{
    @Override
    public int calculateExperience(){
        return 1;
    }
}

class AcceptedRequestGain implements ExperienceStrategy{
    @Override
    public int calculateExperience(){
        return 3;
    }
}
class ContributionGain implements ExperienceStrategy{
    @Override
    public int calculateExperience(){
        return 10;
    }
}
