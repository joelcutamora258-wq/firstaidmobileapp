package com.example.data.datasource

import com.example.data.model.DoAndDontItem
import com.example.data.model.EmergencyToolType
import com.example.data.model.FirstAidStep
import com.example.data.model.FirstAidTopic
import com.example.data.model.SafetyRecommendation
import com.example.data.model.SeverityLevel
import com.example.data.model.TopicCategory
import com.example.data.model.UserProfileContext

object LocalFirstAidData {

  val userProfiles = listOf(
    UserProfileContext(
      id = "outdoor",
      name = "Outdoor & Hiking",
      description = "Wilderness exploration, hiking trails, camping, and remote adventures",
      iconName = "forest"
    ),
    UserProfileContext(
      id = "parent",
      name = "Parent & Toddler Care",
      description = "Home safety, choking prevention, pediatric fever, and playground falls",
      iconName = "child_care"
    ),
    UserProfileContext(
      id = "home",
      name = "Home & Kitchen",
      description = "Cooking burns, cuts, chemical splash, falls, and household accidents",
      iconName = "home"
    ),
    UserProfileContext(
      id = "sports",
      name = "Sports & Athletics",
      description = "Sprains, fractures, concussions, dislocations, and heat dehydration",
      iconName = "fitness_center"
    ),
    UserProfileContext(
      id = "workplace",
      name = "Workplace & Office",
      description = "Ergonomics, fainting, electrical hazards, and CPR/AED readiness",
      iconName = "business"
    ),
    UserProfileContext(
      id = "elderly",
      name = "Senior & Elderly Care",
      description = "Slip/fall injuries, stroke identification (F.A.S.T.), and heart alerts",
      iconName = "elderly"
    )
  )

  val predefinedTopics = listOf(
    FirstAidTopic(
      id = "cpr_adult",
      title = "Adult CPR & AED",
      subtitle = "Cardiopulmonary resuscitation for unresponsive adults",
      category = TopicCategory.RESUSCITATION,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 2,
      emergencyCallPrompt = "CALL 911 / EMS IMMEDIATELY. Send someone to retrieve an AED.",
      redFlags = listOf(
        "Victim is unresponsive and not breathing normally (gasping or no breath)",
        "No detectable pulse within 10 seconds",
        "Cyanosis (blue tint on lips or fingernails)"
      ),
      summary = "Rapid chest compressions keep oxygenated blood flowing to the brain and vital organs until professional medical defibrillation arrives.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Check Responsiveness & Shout for Help",
          description = "Tap the victim's shoulders firmly and shout loudly: 'Are you okay?'. If unresponsive, shout to a bystander: 'You in the blue shirt, call 911 and bring an AED!'"
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Position Hands on Center of Chest",
          description = "Place the heel of one hand in the center of the chest (lower half of breastbone). Interlock your other hand on top. Keep elbows locked straight with your shoulders directly above your hands."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Deliver Hard and Fast Compressions",
          description = "Push down at least 2 inches (5 cm) deep at a rate of 100 to 120 compressions per minute (to the beat of 'Stayin' Alive'). Allow chest to fully recoil between each push.",
          cautionNote = "Do not lean on the chest between compressions. Maintain uninterrupted rhythm.",
          durationSeconds = 120
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Give 2 Rescue Breaths (If Trained)",
          description = "After 30 compressions, tilt head back, lift chin, pinch nose, and deliver 2 gentle breaths (1 second each) until chest visibly rises. If untrained, continue hands-only CPR non-stop."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Apply AED as soon as available",
          description = "Turn on the Automated External Defibrillator (AED) and follow audible voice prompts. Attach pads to bare chest (upper right chest, lower left ribs). Stand clear when analyzing or delivering shock."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO push hard and fast at 100-120 BPM in the center of the chest"),
        DoAndDontItem(true, "DO let the chest fully rise back up after every single compression"),
        DoAndDontItem(true, "DO use an AED immediately if one is available"),
        DoAndDontItem(false, "DO NOT stop compressions for more than 10 seconds"),
        DoAndDontItem(false, "DO NOT hesitate to act—doing CPR is always better than doing nothing")
      ),
      toolType = EmergencyToolType.CPR_METRONOME
    ),

    FirstAidTopic(
      id = "choking_adult",
      title = "Choking (Heimlich Maneuver)",
      subtitle = "Airway obstruction relief for conscious adults and children",
      category = TopicCategory.RESUSCITATION,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 1,
      emergencyCallPrompt = "Call 911 immediately if the victim cannot speak, cough, or breathe, or becomes unconscious.",
      redFlags = listOf(
        "Universal choking sign (hands clutched to throat)",
        "Inability to speak, cry, or make sound",
        "High-pitched wheezing or skin turning blue/purple"
      ),
      summary = "Relieve complete airway blockage using rapid abdominal thrusts to create an artificial cough.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Ask: 'Are you choking?'",
          description = "If the person can cough forcefully or speak, encourage them to keep coughing. If they nod or cannot make sound, tell them you are going to help."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Give 5 Back Blows",
          description = "Stand slightly behind them. Support their upper chest with one hand and lean them forward. Deliver 5 firm, distinct blows between shoulder blades with the heel of your hand."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Give 5 Abdominal Thrusts (Heimlich)",
          description = "Stand behind victim. Wrap arms around waist. Make a fist with thumb side facing inward just above the navel (below ribcage). Grasp fist with other hand and pull sharply inward and upward 5 times."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Alternate 5 Back Blows and 5 Thrusts",
          description = "Continue alternating 5 back blows and 5 abdominal thrusts until the lodged object is expelled or emergency services arrive."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "If Victim Loses Consciousness",
          description = "Lower victim carefully to the ground. Call 911. Begin CPR compressions immediately. Each time you open airway to give breaths, look inside mouth for object and remove if visible."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO lean the person forward so gravity assists in expelling the object"),
        DoAndDontItem(true, "DO position fist slightly above the belly button, well below the breastbone"),
        DoAndDontItem(false, "DO NOT perform blind finger sweeps in the throat, which can push object deeper"),
        DoAndDontItem(false, "DO NOT slap the back while person is standing upright (lean them forward first)")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "severe_bleeding",
      title = "Severe Bleeding & Hemorrhage",
      subtitle = "Arterial and deep wound hemorrhage management",
      category = TopicCategory.TRAUMA,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 3,
      emergencyCallPrompt = "Call 911 for spurting blood, deep wounds that won't stop bleeding after 5 minutes of pressure, or signs of shock.",
      redFlags = listOf(
        "Pulsing or spurting bright red blood (arterial bleeding)",
        "Blood soaking through multiple bandages rapidly",
        "Pale, clammy skin, confusion, or rapid shallow breathing (hypovolemic shock)"
      ),
      summary = "Stop life-threatening blood loss using continuous firm direct pressure and pressure dressings.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Expose Wound & Protect Yourself",
          description = "Put on sterile nitrile gloves if available. Remove or cut clothing to clearly see the source of the bleeding."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Apply Direct Continuous Pressure",
          description = "Cover the wound with sterile gauze, clean cloth, or your gloved hand. Press down firmly with both hands directly over the bleeding point.",
          cautionNote = "Do NOT lift the dressing to peek—lifting breaks the developing blood clot.",
          durationSeconds = 600
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Add More Layers if Blood Soaks Through",
          description = "If blood seeps through, place additional gauze directly on top and continue pressing harder. Do not remove original cloth."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Apply Tourniquet for Severe Limb Bleeding",
          description = "If bleeding from arm or leg cannot be stopped and is life-threatening, apply a commercial tourniquet 2-3 inches above the wound (never over a joint). Tighten windlass until bleeding stops completely. Note exact time applied."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Keep Victim Warm & Treat for Shock",
          description = "Lay victim flat on their back. Elevate legs slightly if no pelvic injury. Cover with a blanket to maintain body temperature."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO press firmly and continuously for at least 10-15 minutes without lifting"),
        DoAndDontItem(true, "DO write down the exact time a tourniquet was applied"),
        DoAndDontItem(false, "DO NOT remove embedded objects (knives, glass)—stabilize around them with rolled bandages"),
        DoAndDontItem(false, "DO NOT loosen or remove a tourniquet once tightened—let hospital staff handle it")
      ),
      toolType = EmergencyToolType.PRESSURE_TIMER
    ),

    FirstAidTopic(
      id = "burns_scalds",
      title = "Burns & Scalds",
      subtitle = "Thermal, boiling liquid, and electrical burn care",
      category = TopicCategory.TRAUMA,
      severity = SeverityLevel.URGENT,
      estimatedMinutes = 15,
      emergencyCallPrompt = "Call 911 for 3rd degree burns (white/charred), burns larger than 3 inches, burns to face, hands, groin, or major joints, or chemical/electrical burns.",
      redFlags = listOf(
        "Leathery, charred, or blackened skin (3rd degree)",
        "Burn covers face, hands, feet, or genitals",
        "Victim inhaled smoke or has scorched nasal hairs"
      ),
      summary = "Cool the burn immediately with running cool water to stop thermal skin damage, then protect with a non-stick sterile covering.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Remove Heat Source & Ensure Safety",
          description = "Extinguish flames, turn off power, or separate victim from boiling liquid. Remove jewelry, rings, and tight clothing near the burned area before swelling occurs."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Cool Burn Under Running Water for 10-20 Minutes",
          description = "Hold the burned skin under gentle, cool running tap water (15°C–20°C / 59°F–68°F) for at least 10 to 20 minutes. This draws heat out of deeper tissue.",
          cautionNote = "Never use ice or freezing water. Ice causes tissue ischemia and severe frostbite damage.",
          durationSeconds = 900
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Cover with Sterile Non-Stick Dressing or Plastic Wrap",
          description = "Loosely cover the burned area with a sterile non-adherent dressing or clean food-grade plastic cling wrap (layer lightly, do not wrap tightly around a limb)."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Protect Blisters & Manage Pain",
          description = "Leave blisters completely intact to prevent infection. Provide over-the-counter pain relief (acetaminophen or ibuprofen) if conscious and able to swallow."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO cool immediately with cool running tap water for 10 to 20 minutes"),
        DoAndDontItem(true, "DO remove rings and bracelets quickly before swelling begins"),
        DoAndDontItem(false, "DO NOT apply ice, butter, oil, mayonnaise, toothpaste, or ointments to raw burns"),
        DoAndDontItem(false, "DO NOT pop or puncture burn blisters")
      ),
      toolType = EmergencyToolType.BURN_COOLING_TIMER
    ),

    FirstAidTopic(
      id = "anaphylaxis_epipen",
      title = "Anaphylaxis (Severe Allergy)",
      subtitle = "Life-threatening allergic reaction & EpiPen auto-injector guide",
      category = TopicCategory.ALLERGIC,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 2,
      emergencyCallPrompt = "CALL 911 IMMEDIATELY. Anaphylaxis is a rapid medical emergency requiring epinephrine.",
      redFlags = listOf(
        "Difficulty breathing, wheezing, or tightness in throat",
        "Swelling of tongue, lips, face, or throat",
        "Dizziness, feeling faint, or sudden collapse after allergen exposure (nuts, bee sting, medication)"
      ),
      summary = "Administer epinephrine auto-injector into outer mid-thigh without delay, position victim comfortably, and await paramedics.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Call 911 & Locate Epinephrine Auto-Injector",
          description = "Call emergency services right away. Ask the person or bystanders for their epinephrine auto-injector (EpiPen, Auvi-Q, etc.)."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Prepare the Auto-Injector",
          description = "Grasp injector firmly in fist with orange/needle end pointing down. Pull off the blue safety release cap with your other hand. Do not place thumb over ends."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Inject into Outer Mid-Thigh",
          description = "Hold injector 90 degrees against the outer mid-thigh. Push firmly until you hear or feel a 'click'. Hold firmly in place for 3 full seconds.",
          cautionNote = "Can be injected through lightweight clothing if necessary. Never inject into buttocks or veins.",
          durationSeconds = 3
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Massage Site & Lay Patient Flat",
          description = "Remove injector safely and massage the injection site for 10 seconds. Lay victim flat on back with feet elevated. If breathing is difficult, allow them to sit up slightly."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Monitor for Second Wave (Biphasic Reaction)",
          description = "If symptoms do not improve within 5 to 15 minutes and EMS has not arrived, a second dose of epinephrine may be administered using a new auto-injector."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO inject into the middle outer thigh where large muscle absorbs epinephrine quickly"),
        DoAndDontItem(true, "DO stay with the person—symptoms can rebound rapidly"),
        DoAndDontItem(false, "DO NOT have the victim stand up or walk, which can cause sudden fatal blood pressure drop"),
        DoAndDontItem(false, "DO NOT wait to see if antihistamines (Benadryl) work before using epinephrine")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "fracture_sprain",
      title = "Fractures, Dislocations & Sprains",
      subtitle = "Bone fracture stabilization & R.I.C.E. protocol for sprains",
      category = TopicCategory.TRAUMA,
      severity = SeverityLevel.URGENT,
      estimatedMinutes = 5,
      emergencyCallPrompt = "Call 911 for open fractures (bone pierced skin), visible deformities, numbness/loss of pulse below injury, or pelvic/femur fractures.",
      redFlags = listOf(
        "Bone visible protruding through skin (open fracture)",
        "Severe deformity, unnatural angle, or bone grinding sensation (crepitus)",
        "Fingers or toes turn blue, cold, or completely numb below the injury"
      ),
      summary = "Immobilize the injured limb in the position found, prevent movement, and apply R.I.C.E. (Rest, Ice, Compression, Elevation).",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Immobilize the Injured Area",
          description = "Support the limb and keep it completely still. Do NOT try to push bones back into place or straighten a deformed joint."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Control Bleeding on Open Fractures",
          description = "If skin is broken, cover wound with sterile dressing and apply gentle pressure to wound edges. Avoid pressing directly on protruding bone ends."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Apply Splint Above & Below the Joint",
          description = "Use a padded rigid object (SAM splint, board, rolled magazine) spanning past the joints above and below the fracture. Secure gently with bandages or cloth strips."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Apply Cold Pack (For Closed Sprains/Strains)",
          description = "Wrap ice or cold gel pack in a thin towel and apply for 15-20 minutes every 2 hours to minimize swelling.",
          cautionNote = "Never apply ice directly against bare skin.",
          durationSeconds = 900
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Elevate Above Heart Level (If Pain Allows)",
          description = "Prop up sprained ankle or wrist on cushions above heart level to reduce throbbing and fluid accumulation."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO check pulse, sensation, and warmth in fingers/toes after applying a splint"),
        DoAndDontItem(true, "DO support the limb gently with soft padding"),
        DoAndDontItem(false, "DO NOT try to reset, manipulate, or straighten broken bones"),
        DoAndDontItem(false, "DO NOT tie splints so tightly that blood circulation is cut off")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "heatstroke_exhaustion",
      title = "Heat Stroke & Heat Exhaustion",
      subtitle = "Critical heat illness differentiation and active rapid cooling",
      category = TopicCategory.ENVIRONMENTAL,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 5,
      emergencyCallPrompt = "CALL 911 IMMEDIATELY for Heat Stroke (hot red skin, confusion, seizures, body temp > 103°F / 39.4°C).",
      redFlags = listOf(
        "Confusion, slurred speech, delirium, or loss of consciousness",
        "Hot, dry skin OR heavy sweating with high core fever",
        "Vomiting, seizures, or rapid racing pulse"
      ),
      summary = "Rapidly cool the patient using cold water immersion or ice packs on neck, armpits, and groin to prevent brain and organ damage.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Move to Cool, Shaded Area Immediately",
          description = "Bring the person into air conditioning or the coolest available shade. Remove unnecessary outer clothing."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Begin Aggressive Active Cooling",
          description = "Immerse in cold water tub if possible. Alternatively, douse with cold water and fan vigorously. Place ice packs wrapped in cloth on neck, armpits, and groin."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Provide Hydration ONLY If Conscious & Alert",
          description = "If alert and not vomiting, give small sips of cool water or electrolyte drink. If confused, drowsy, or unconscious, give NO fluids by mouth."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Monitor Breathing & Recovery Position",
          description = "If victim becomes unconscious, place them in the side recovery position to keep airway clear. Continue cooling until EMS arrives."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO cool first, transport second for exertional heat stroke"),
        DoAndDontItem(true, "DO place cold packs in areas with high blood flow (neck, armpits, groin)"),
        DoAndDontItem(false, "DO NOT give fluids to someone who is drowsy or vomiting"),
        DoAndDontItem(false, "DO NOT give aspirin or acetaminophen—they do not work for environmental heat stroke")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "seizures_epilepsy",
      title = "Seizures & Convulsions",
      subtitle = "Protecting a seizing individual & post-ictal recovery protocol",
      category = TopicCategory.RESUSCITATION,
      severity = SeverityLevel.URGENT,
      estimatedMinutes = 5,
      emergencyCallPrompt = "Call 911 if seizure lasts longer than 5 minutes, if another seizure follows immediately, if pregnant, or if breathing doesn't return.",
      redFlags = listOf(
        "Seizure activity lasts longer than 5 minutes (status epilepticus)",
        "Victim is injured during the fall or was in water",
        "Victim does not wake up or has difficulty breathing after seizure stops"
      ),
      summary = "Protect the person from physical harm during convulsions, keep airway open, and never place anything in their mouth.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Clear the Surrounding Area",
          description = "Move hard, sharp, or hot objects away. Cushion their head with something soft (folded jacket, pillow). Loosen tight neckwear."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Time the Seizure Duration",
          description = "Look at your watch or phone to record exact start time. If seizure exceeds 5 minutes, call 911 immediately."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Do NOT Restrain the Person",
          description = "Allow the seizure to run its course. Do not hold them down or try to stop muscle jerking."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Roll into Recovery Position After Jerking Stops",
          description = "Once convulsions subside, roll person gently onto their side (recovery position) to prevent saliva from blocking airway."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Stay Calm & Reassure as They Awaken",
          description = "The person will likely be disoriented and exhausted (post-ictal state). Speak softly and stay until fully recovered."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO cushion the head and roll person onto their side after movement ends"),
        DoAndDontItem(true, "DO time the exact length of the seizure"),
        DoAndDontItem(false, "DO NOT put anything in the person's mouth (they cannot swallow their tongue)"),
        DoAndDontItem(false, "DO NOT hold the person down or try to restrain spasms")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "snake_spider_bite",
      title = "Snake & Spider Bites",
      subtitle = "Venomous bite immobilization and wilderness response",
      category = TopicCategory.WILDERNESS,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 5,
      emergencyCallPrompt = "Call 911 or emergency medical transport immediately for suspected venomous snake or spider bites.",
      redFlags = listOf(
        "Two distinct fang puncture marks with rapid swelling and severe pain",
        "Nausea, sweating, numbness, vision disturbances, or muscle twitching",
        "Difficulty swallowing or breathing"
      ),
      summary = "Keep the victim calm, immobilize the affected limb below heart level, and seek emergency antivenom.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Move Away to Safety & Stay Calm",
          description = "Ensure snake has retreated. Keep victim completely calm and still—activity accelerates venom circulation through bloodstream."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Remove Rings, Shoes & Tight Clothing",
          description = "Remove all jewelry and constrictive items from the bitten extremity immediately before rapid swelling sets in."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Immobilize the Bitten Limb Below Heart Level",
          description = "Splint the arm or leg loosely to keep it still. Position the bite site at or slightly below the level of the heart."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Clean Gently with Soap & Water",
          description = "Wash the wound gently with clean water. Cover with a clean, dry, loose sterile bandage."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "Note Snake Description / Photo Safely",
          description = "If safe, remember color and head shape (or take a photo from a safe distance) to help doctors select correct antivenom."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO keep victim resting completely still and quiet"),
        DoAndDontItem(true, "DO remove rings and watches immediately"),
        DoAndDontItem(false, "DO NOT cut the wound or try to suck out venom with your mouth"),
        DoAndDontItem(false, "DO NOT apply a tight arterial tourniquet or ice to snake bites")
      ),
      toolType = EmergencyToolType.NONE
    ),

    FirstAidTopic(
      id = "poisoning_chemical",
      title = "Poisoning & Chemical Ingestion",
      subtitle = "Household toxins, medication overdose & corrosive chemical ingestion",
      category = TopicCategory.ALLERGIC,
      severity = SeverityLevel.CRITICAL,
      estimatedMinutes = 2,
      emergencyCallPrompt = "Call Poison Control (1-800-222-1222 in US) or dial 911 immediately.",
      redFlags = listOf(
        "Victim is unconscious, having seizures, or struggling to breathe",
        "Burns around mouth or throat from caustic chemicals (bleach, drain cleaner)",
        "Suspected deliberate overdose or unknown chemical ingestion"
      ),
      summary = "Identify the toxin container, call Poison Help, and follow specialist guidance. Never induce vomiting unless explicitly told.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Check Responsiveness & Breathing",
          description = "If victim is collapsed or not breathing, call 911 and begin CPR immediately. Use a face shield to avoid personal poison exposure."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Call Poison Control Center (1-800-222-1222)",
          description = "Have the product container, bottle, or packaging in hand. Report the victim's age, weight, substance, estimated amount, and time ingested."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "For Swallowed Poisons: Do NOT Induce Vomiting",
          description = "Vomiting caustic acids or alkalis causes second chemical burns to esophagus and airway. Do not give ipecac syrup or salt water."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "For Inhaled Poisons: Move to Fresh Air",
          description = "Get person into open outdoor air immediately without endangering yourself. Open windows and doors."
        ),
        FirstAidStep(
          stepNumber = 5,
          title = "For Skin / Eye Chemical Splash",
          description = "Flush skin or eyes with continuous lukewarm running water for at least 15 minutes. Remove contaminated clothing."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO keep the poison packaging or pill bottle ready for emergency responders"),
        DoAndDontItem(true, "DO call Poison Help (1-800-222-1222) immediately for free expert guidance"),
        DoAndDontItem(false, "DO NOT induce vomiting unless specifically instructed by Poison Control"),
        DoAndDontItem(false, "DO NOT try home remedies like milk or charcoal without medical direction")
      ),
      toolType = EmergencyToolType.EYE_FLUSH_TIMER
    ),

    FirstAidTopic(
      id = "concussion_head_trauma",
      title = "Concussion & Head Trauma",
      subtitle = "Assessing traumatic brain injury & intracranial pressure warning signs",
      category = TopicCategory.TRAUMA,
      severity = SeverityLevel.URGENT,
      estimatedMinutes = 5,
      emergencyCallPrompt = "Call 911 for loss of consciousness, repeated vomiting, worsening headache, unequal pupils, or clear fluid from ears/nose.",
      redFlags = listOf(
        "Loss of consciousness (even briefly)",
        "Unequal pupil sizes or slurred speech",
        "Clear fluid or bleeding draining from ears or nose (skull fracture)"
      ),
      summary = "Stabilize the head and neck to prevent cervical spine injury, monitor consciousness, and seek medical evaluation.",
      steps = listOf(
        FirstAidStep(
          stepNumber = 1,
          title = "Immobilize Head & Neck",
          description = "Hold the victim's head still with both hands in the position found. Prevent them from twisting or turning their neck."
        ),
        FirstAidStep(
          stepNumber = 2,
          title = "Control Scalp Bleeding with Light Pressure",
          description = "Cover scalp wounds with sterile gauze. Apply gentle pressure around the wound, avoiding deep depression if a depressed skull fracture is suspected."
        ),
        FirstAidStep(
          stepNumber = 3,
          title = "Perform Concussion Symptom Check",
          description = "Ask questions to assess memory: 'Where are we?', 'What day is it?'. Check for dizziness, sensitivity to light/sound, or nausea."
        ),
        FirstAidStep(
          stepNumber = 4,
          title = "Do NOT Return to Activity",
          description = "Strict physical and cognitive rest. Never allow an athlete with suspected concussion back into the game."
        )
      ),
      dosAndDonts = listOf(
        DoAndDontItem(true, "DO keep head, neck, and spine aligned and immobilized"),
        DoAndDontItem(true, "DO monitor mental status continuously for 24-48 hours"),
        DoAndDontItem(false, "DO NOT move victim if neck or spinal injury is suspected unless in immediate environmental danger"),
        DoAndDontItem(false, "DO NOT give aspirin or NSAIDs which can worsen internal brain bleeding")
      ),
      toolType = EmergencyToolType.NONE
    )
  )

  val defaultRecommendations = listOf(
    SafetyRecommendation(
      id = "rec_cpr",
      title = "CPR & AED Refresher",
      category = "Resuscitation",
      reason = "Essential life-saving skill for every household and workplace.",
      actionAdvice = "Practice the 100-120 BPM rhythm and locate nearest AEDs in your building.",
      relatedQuery = "Adult CPR & AED"
    ),
    SafetyRecommendation(
      id = "rec_burns",
      title = "Kitchen Burns & Scalds Protocol",
      category = "Trauma",
      reason = "One of the most frequent domestic emergency accidents.",
      actionAdvice = "Keep cool running water ready and avoid applying butter or oils.",
      relatedQuery = "Burns & Scalds"
    ),
    SafetyRecommendation(
      id = "rec_choking",
      title = "Choking Relief & Heimlich Technique",
      category = "Resuscitation",
      reason = "Critical emergency requiring immediate action within 60 seconds.",
      actionAdvice = "Learn the difference between partial cough and complete airway blockage.",
      relatedQuery = "Choking (Heimlich Maneuver)"
    ),
    SafetyRecommendation(
      id = "rec_bleeding",
      title = "Severe Bleeding & Pressure Dressing",
      category = "Trauma",
      reason = "Stopping rapid blood loss prevents fatal hypovolemic shock.",
      actionAdvice = "Keep sterile gauze and compression wraps in your vehicle and home kits.",
      relatedQuery = "Severe Bleeding & Hemorrhage"
    )
  )
}
