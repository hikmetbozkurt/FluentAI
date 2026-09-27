#!/usr/bin/env python3
"""
Comprehensive Exercise Generator for Vocabulary (50 items across A2-C2).
"""
from mcq_balancer import McqBalancer

balancer = McqBalancer(seed=706)

def make_ex(ex_id, target_id, level, prompt_en, prompt_tr, stem, correct, distractors, exp_en, exp_tr, dist_exps, diff="standard"):
    options, idx = balancer.balance_options(correct, distractors)
    return {
        "id": ex_id,
        "target_content_id": target_id,
        "cefr_level": level,
        "skill_domain": "vocabulary",
        "exercise_type": "multiple_choice",
        "prompt_en": prompt_en,
        "prompt_tr_hint": prompt_tr,
        "stem": stem,
        "options": options,
        "correct_answer": correct,
        "explanation_en": exp_en,
        "explanation_tr": exp_tr,
        "distractor_explanations": dist_exps,
        "difficulty": diff,
        "status": "APPROVED",
        "version": 1
    }

VOCAB_EXERCISES = []

VOCAB_EXERCISES.append(make_ex('exercise.vocab.ache-01', 'vocab.ache-verb', 'A2',
    'Choose the correct word to complete the health statement:', 'Cümledeki sağlık ve ağrı bağlamına en uygun kelimeyi seçiniz.', 'After sitting in front of the laptop screen for ten hours, my neck and shoulders began to ___ .',
    'ache', ['freeze', 'cough', 'repair'],
    "'Ache' means to suffer a continuous, dull pain.", "'Ache', vücutta sürekli ve sızlayan bir ağrı hissetmek demektir.",
    {'freeze': "'Freeze' donmak demektir; kas ağrısını ifade etmez.", 'cough': "'Cough' öksürmek demektir.", 'repair': "'Repair' tamir etmek demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.butter-01', 'vocab.butter', 'A2',
    'Choose the correct ingredient word to complete the recipe:', 'Tarifi tamamlayan doğru yiyecek malzemesini seçiniz.', 'She spread some cold ___ on the freshly toasted sourdough bread.',
    'butter', ['flour', 'vinegar', 'mustard'],
    "'Butter' is a pale yellow edible fatty substance made by churning cream.", "'Butter', kızarmış ekmeğe sürülen tereyağıdır.",
    {'flour': "'Flour' un demektir; ekmeğin üzerine sürülmez.", 'vinegar': "'Vinegar' sirke demektir.", 'mustard': "'Mustard' hardal demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.cough-01', 'vocab.cough-v', 'A2',
    'Choose the correct verb for this symptom:', 'Semptomu anlatan doğru fiili seçiniz.', 'The dust in the old archive room made everyone ___ loudly.',
    'cough', ['ache', 'greet', 'whisper'],
    "'Cough' means to expel air from the lungs suddenly with a sharp sound.", "'Cough', tozdan veya hastalıktan öksürmek demektir.",
    {'ache': "'Ache' sızlamak demektir.", 'greet': "'Greet' selamlamak demektir.", 'whisper': "'Whisper' fısıldamak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.empty-01', 'vocab.empty', 'A2',
    'Choose the correct adjective to describe the space:', 'Alanı niteleyen doğru sıfatı seçiniz.', 'The office was completely ___ at 9 PM because all employees had gone home.',
    'empty', ['crowded', 'narrow', 'noisy'],
    "'Empty' means containing nothing; not filled or occupied.", "'Empty' (boş), içinde kimse veya hiçbir şey olmayan durumdur.",
    {'crowded': "'Crowded' kalabalık demektir; herkes gittikten sonra ofis kalabalık olamaz.", 'narrow': "'Narrow' dar demektir.", 'noisy': "'Noisy' gürültülü demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.greet-01', 'vocab.greet', 'A2',
    'Choose the correct verb for welcoming a guest:', 'Misafiri karşılamayı ifade eden doğru fiili seçiniz.', 'The receptionist stood up with a warm smile to ___ the international delegates.',
    'greet', ['dismiss', 'scold', 'ignore'],
    "'Greet' means to give a polite word or sign of welcome upon meeting someone.", "'Greet', birini nezaketle karşılamak ve selamlamak demektir.",
    {'dismiss': "'Dismiss' başından savmak veya kovmak demektir.", 'scold': "'Scold' azarlamak demektir.", 'ignore': "'Ignore' görmezden gelmek demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.local-01', 'vocab.local-adj', 'A2',
    'Choose the correct adjective describing nearby community resources:', 'Yakındaki yerel işletmeleri anlatan doğru sıfatı seçiniz.', "We always buy our fresh produce from the ___ farmers' market down the street.",
    'local', ['remote', 'foreign', 'distant'],
    "'Local' relating or belonging to an area, a neighborhood, or a particular district.", "'Local', yerel/mahalledeki işletmeleri ve kaynakları ifade eder.",
    {'remote': "'Remote' uzak veya ıssız demektir.", 'foreign': "'Foreign' yabancı demektir.", 'distant': "'Distant' uzakta olan demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.passenger-01', 'vocab.passenger', 'A2',
    'Choose the correct word for a traveler in a vehicle:', 'Araçtaki yolcuyu ifade eden doğru kelimeyi seçiniz.', 'Every ___ on the high-speed train must show a valid digital ticket.',
    'passenger', ['pedestrian', 'mechanic', 'conductor'],
    "'Passenger' is a traveler on a public or private conveyance not operating the vehicle.", "'Passenger', tren, uçak veya otobüsteki yolcudur.",
    {'pedestrian': "'Pedestrian' yaya demektir.", 'mechanic': "'Mechanic' tamirci demektir.", 'conductor': "'Conductor' bilet kontrolörü veya kondüktördür."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.relative-01', 'vocab.relative', 'A2',
    'Choose the correct word for family kinship:', 'Akrabalık bağını anlatan doğru kelimeyi seçiniz.', 'My uncle invited every close ___ to his sixtieth birthday celebration in Izmir.',
    'relative', ['stranger', 'colleague', 'competitor'],
    "'Relative' is a person connected by blood or marriage.", "'Relative', aileden olan akraba veya hısım demektir.",
    {'stranger': "'Stranger' yabancı/tanınmayan kişi demektir.", 'colleague': "'Colleague' iş arkadaşı demektir.", 'competitor': "'Competitor' rakip demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.silent-01', 'vocab.silent', 'A2',
    'Choose the correct adjective for absolute quietness:', 'Sessizliği anlatan doğru sıfatı seçiniz.', 'Please keep your mobile phones on ___ mode during the executive keynote.',
    'silent', ['loud', 'vocal', 'furious'],
    "'Silent' means completely quiet or making no sound.", "'Silent', telefonun sessiz modunu ifade eden sıfattır.",
    {'loud': "'Loud' yüksek sesli demektir.", 'vocal': "'Vocal' sesli veya sözlü demektir.", 'furious': "'Furious' öfkeli demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.timetable-01', 'vocab.timetable', 'A2',
    'Choose the correct word for a transportation schedule:', 'Ulaşım sefer çizelgesini anlatan doğru kelimeyi seçiniz.', 'Check the commuter train ___ to see when the next express train departs for Ankara.',
    'timetable', ['receipt', 'menu', 'billboard'],
    "'Timetable' is a chart showing the departure and arrival times of trains or buses.", "'Timetable', tren ve otobüslerin saat çizelgesi / sefer tarifesidir.",
    {'receipt': "'Receipt' makbuz/fiş demektir.", 'menu': "'Menu' yemek menüsüdür.", 'billboard': "'Billboard' reklam panosudur."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.a-pinch-of-01', 'vocab.a-pinch-of', 'B1',
    'Choose the correct idiomatic quantity expression for cooking:', 'Yemek pişirmede küçük bir tutamı anlatan doğru ölçü ifadesini seçiniz.', 'Add just ___ sea salt to enhance the flavor of the caramel sauce.',
    'a pinch of', ['a gallon of', 'a loaf of', 'a crowd of'],
    "'A pinch of' refers to an amount of an ingredient that can be held between thumb and forefinger.", "'A pinch of', baş parmak ile işaret parmağı arasında tutulan 'bir tutam / bir fiske' miktarını anlatır.",
    {'a gallon of': 'Galon sıvı hacim birimidir, tuza uymaz.', 'a loaf of': 'Somun ekmek için kullanılır.', 'a crowd of': 'İnsan kalabalığı için kullanılır.'}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.campus-01', 'vocab.campus', 'B1',
    'Choose the correct noun for university grounds:', 'Üniversite yerleşkesini anlatan doğru ismi seçiniz.', 'The new engineering library is located in the northern sector of the university ___ .',
    'campus', ['pavement', 'harbor', 'runway'],
    "'Campus' the grounds and buildings of a university or college.", "'Campus', üniversite yerleşkesidir.",
    {'pavement': "'Pavement' kaldırım demektir.", 'harbor': "'Harbor' liman demektir.", 'runway': "'Runway' uçak pistidir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.contribute-01', 'vocab.contribute', 'B1',
    'Choose the correct verb for providing input to a team project:', 'Projeye katkı sağlamayı ifade eden doğru fiili seçiniz.', 'Every software developer on the scrum team is encouraged to ___ ideas during sprint planning.',
    'contribute', ['withhold', 'distract', 'confiscate'],
    "'Contribute' means to give or supply in common with others.", "'Contribute', ortak bir amaca fikir veya emekle katkıda bulunmaktır.",
    {'withhold': "'Withhold' saklamak veya esirgemek demektir.", 'distract': "'Distract' dikkat dağıtmak demektir.", 'confiscate': "'Confiscate' el koymak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.eat-in-01', 'vocab.eat-in', 'B1',
    'Choose the correct phrasal verb for dining at home or in a restaurant:', 'Dışarıdan almak yerine mekanda/evde oturup yemeyi anlatan doğru deyimsel fiili seçiniz.', 'Would you like to take your sandwich to go, or would you prefer to ___ ?',
    'eat in', ['eat out', 'look out', 'drop off'],
    "'Eat in' means to consume food on the premises of a restaurant or at home.", "'Eat in', restoranda oturup yemek veya evde yemek anlamına gelir.",
    {'eat out': "'Eat out' dışarıda restorana yemeğe gitmektir.", 'look out': "'Look out' dikkat etmek demektir.", 'drop off': "'Drop off' birini bir yere bırakmaktır."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.guided-tour-01', 'vocab.guided-tour', 'B1',
    'Choose the correct modifier for an informative tour:', 'Rehber eşliğinde yapılan geziyi anlatan doğru kelimeyi seçiniz.', 'The international visitors booked a ___ tour of Topkapi Palace with an art historian.',
    'guided', ['blind', 'hasty', 'reckless'],
    "'Guided' led or accompanied by a professional guide.", "'Guided tour', uzman rehber eşliğinde yapılan turlardır.",
    {'blind': "'Blind' kör demektir.", 'hasty': "'Hasty' aceleci demektir.", 'reckless': "'Reckless' pervasız demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.lock-up-01', 'vocab.lock-up', 'B1',
    'Choose the correct phrasal verb for securing a building at the end of the day:', 'Günün sonunda ofisi kilitleyip kapatmayı anlatan doğru deyimsel fiili seçiniz.', 'As the last person leaving the laboratory, it is your responsibility to ___ all doors and windows.',
    'lock up', ['set up', 'give up', 'break up'],
    "'Lock up' means to secure a building completely by locking all doors.", "'Lock up', bir mekanın tüm kapılarını kilitleyip güvene almaktır.",
    {'set up': "'Set up' kurmak demektir.", 'give up': "'Give up' vazgeçmek demektir.", 'break up': "'Break up' ayrılmak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.pack-away-01', 'vocab.pack-away', 'B1',
    'Choose the correct phrasal verb for storing items neatly after use:', 'Kullanım sonrası eşyaları toplayıp kaldırmayı anlatan doğru fiili seçiniz.', 'After the workshop concluded, the facilitators helped ___ the laptops and projection cables.',
    'pack away', ['throw away', 'run away', 'pass away'],
    "'Pack away' means to put something into a container or place of storage after use.", "'Pack away', eşyaları toplayıp dolaba/yerine kaldırmaktır.",
    {'throw away': "'Throw away' çöpe atmak demektir.", 'run away': "'Run away' kaçmak demektir.", 'pass away': "'Pass away' vefat etmek demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.radiator-01', 'vocab.radiator', 'B1',
    'Choose the correct heating fixture noun:', 'Isıtma cihazını anlatan doğru ismi seçiniz.', 'The office was chilly because the cast-iron ___ beneath the window had developed an air lock.',
    'radiator', ['refrigerator', 'incubator', 'ventilator'],
    "'Radiator' is an appliance consisting of pipes through which steam or hot water passes for heating a room.", "'Radiator', kalorifer peteğidir.",
    {'refrigerator': "'Refrigerator' buzdolabıdır.", 'incubator': "'Incubator' kuluçka makinesidir.", 'ventilator': "'Ventilator' solunum cihazıdır."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.sculpture-01', 'vocab.sculpture', 'B1',
    'Choose the correct art form noun:', 'Üç boyutlu sanat eserini anlatan doğru ismi seçiniz.', 'A magnificent bronze ___ representing human perseverance was unveiled in the corporate courtyard.',
    'sculpture', ['novel', 'symphony', 'sonnet'],
    "'Sculpture' the art of making two- or three-dimensional representative or abstract forms, especially by carving or casting.", "'Sculpture', bronz veya mermer heykeldir.",
    {'novel': "'Novel' romandır.", 'symphony': "'Symphony' senfonidir.", 'sonnet': "'Sonnet' sonedir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.supermarket-cart-01', 'vocab.supermarket-cart', 'B1',
    'Choose the correct item for holding retail groceries:', 'Alışveriş arabasını anlatan doğru tamlamayı seçiniz.', 'She pushed the heavy ___ through the organic produce aisle, filling it with fresh greens.',
    'supermarket cart', ['wheelbarrow', 'stretcher', 'forklift'],
    "'Supermarket cart' a wheeled cart provided by supermarkets for holding customer purchases.", "'Supermarket cart', market alışveriş arabasıdır.",
    {'wheelbarrow': "'Wheelbarrow' inşaat el arabasıdır.", 'stretcher': "'Stretcher' sedyedir.", 'forklift': "'Forklift' yük kaldırma aracıdır."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.a-work-of-art-01', 'vocab.a-work-of-art', 'B2',
    'Choose the appropriate idiom for exceptional creative craftsmanship:', 'Ustalıkla yapılmış bir şaheseri anlatan doğru deyimsel ifadeyi seçiniz.', "The architect's design for the new opera house was universally praised as ___ .",
    'a work of art', ['a wild goose chase', 'a bolt from the blue', 'a drop in the ocean'],
    "'A work of art' something of outstanding beauty, skill, or craftsmanship.", "'A work of art', olağanüstü estetik ve ustalık taşıyan sanat eseri veya şaheserdir.",
    {'a wild goose chase': 'Nafile ve sonuçsuz çaba demektir.', 'a bolt from the blue': 'Beklenmedik şok edici olay demektir.', 'a drop in the ocean': 'Okyanusta bir damla (önemsiz miktar) demektir.'}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.caramelize-01', 'vocab.caramelize', 'B2',
    'Choose the correct culinary culinary chemistry verb:', 'Şekerin ısıyla rengini ve lezzetini dönüştürmeyi anlatan doğru fiili seçiniz.', 'Slowly sauté the sliced red onions over low heat until they ___ into a rich golden brown.',
    'caramelize', ['solidify', 'evaporate', 'freeze'],
    "'Caramelize' to turn sugar or natural sugars in food into caramel by gentle heating.", "'Caramelize', şekerin ısıyla kahverengileşip tatlanması sürecidir.",
    {'solidify': "'Solidify' katılaşmak demektir.", 'evaporate': "'Evaporate' buharlaşmak demektir.", 'freeze': "'Freeze' donmak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.cost-an-arm-and-a-leg-01', 'vocab.cost-an-arm-and-a-leg', 'B2',
    'Choose the idiom for an exorbitant expense:', 'Ateş pahası olmayı anlatan doğru deyimi seçiniz.', 'Booking business-class flights on short notice will ___ , so we should travel economy.',
    'cost an arm and a leg', ['break the ice', 'hit the nail on the head', 'burn the midnight oil'],
    "'Cost an arm and a leg' to be extremely expensive.", "'Cost an arm and a leg', bir servete mal olmak / ateş pahası olmak demektir.",
    {'break the ice': 'Buzları eritip samimiyet kurmak demektir.', 'hit the nail on the head': 'Tam üstüne basmak / doğru tespit yapmak demektir.', 'burn the midnight oil': 'Gece geç saatlere kadar çalışmak demektir.'}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.educate-01', 'vocab.educate', 'B2',
    'Choose the correct verb for fostering professional skills and awareness:', 'Çalışanları eğitip bilinçlendirmeyi anlatan doğru fiili seçiniz.', 'Our human resources team launched a comprehensive campaign to ___ staff on cybersecurity risks.',
    'educate', ['reprimand', 'intimidate', 'deceive'],
    "'Educate' to provide with intellectual, moral, or social instruction.", "'Educate', personeli eğitmek ve bilinçlendirmektir.",
    {'reprimand': "'Reprimand' kınamak veya azarlamak demektir.", 'intimidate': "'Intimidate' gözdağı vermek demektir.", 'deceive': "'Deceive' aldatmak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.framework-01', 'vocab.framework', 'B2',
    'Choose the correct conceptual structure noun:', 'Kavramsal veya yazılımsal çatıyı anlatan doğru ismi seçiniz.', 'Our engineering team selected Flutter as the primary mobile development ___ for cross-platform apps.',
    'framework', ['barrier', 'hurdle', 'culprit'],
    "'Framework' an essential supporting structure of a building, vehicle, or software system.", "'Framework', yazılım veya kavramsal çerçeve / çatıdır.",
    {'barrier': "'Barrier' engel/bariyer demektir.", 'hurdle': "'Hurdle' engel/güçlük demektir.", 'culprit': "'Culprit' suçlu/sorun kaynağı demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.infuse-01', 'vocab.infuse', 'B2',
    'Choose the correct verb for imparting flavor or values:', 'Özünü içine katmayı veya aşılamayı anlatan doğru fiili seçiniz.', 'The new creative director aimed to ___ the heritage fashion brand with modern sustainability values.',
    'infuse', ['drain', 'deprive', 'extract'],
    "'Infuse' to fill, pervade, or soak in liquid to extract flavor.", "'Infuse', bir kuruma değer veya bir karışıma tat aşılamak/katmaktır.",
    {'drain': "'Drain' kurutmak veya boşaltmak demektir.", 'deprive': "'Deprive' yoksun bırakmak demektir.", 'extract': "'Extract' çekip çıkarmak demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.mutual-respect-01', 'vocab.mutual-respect', 'B2',
    'Choose the correct compound noun for productive professional collaboration:', 'Karşılıklı saygıyı ifade eden doğru tamlamayı seçiniz.', 'A high-performing distributed team depends fundamentally on ___ between engineers and product managers.',
    'mutual respect', ['ruthless rivalry', 'bitter hostility', 'blind obedience'],
    "'Mutual respect' reciprocal esteem and consideration between parties.", "'Mutual respect', taraflar arasındaki karşılıklı saygıdır.",
    {'ruthless rivalry': 'Acımasız rekabet demektir.', 'bitter hostility': 'Koyu düşmanlık demektir.', 'blind obedience': 'Körlemesine itaat demektir.'}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.prospectus-01', 'vocab.prospectus', 'B2',
    'Choose the correct noun for a formal informational document for investors or students:', 'Yatırımcılara veya öğrencilere yönelik resmi tanıtım broşürünü anlatan doğru ismi seçiniz.', "Before investing in the tech initial public offering, read the company's financial ___ carefully.",
    'prospectus', ['receipt', 'prescription', 'itinerary'],
    "'Prospectus' a printed booklet advertising a school or summarizing an enterprise for investors.", "'Prospectus', halka arz veya üniversite tanıtım kılavuzu / izahnamedir.",
    {'receipt': "'Receipt' fiş/makbuzdur.", 'prescription': "'Prescription' doktor reçetesidir.", 'itinerary': "'Itinerary' seyahat planıdır."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.sedentary-01', 'vocab.sedentary', 'B2',
    'Choose the correct health adjective describing inactive office lifestyles:', 'Hareketsiz masa başı yaşam tarzını anlatan doğru sıfatı seçiniz.', 'Health professionals emphasize that a prolonged ___ lifestyle increases cardiovascular risks.',
    'sedentary', ['rigorous', 'dynamic', 'strenuous'],
    "'Sedentary' characterized by or requiring a sitting posture with little physical exercise.", "'Sedentary', hareketsiz, masa başı sedanter yaşam tarzıdır.",
    {'rigorous': "'Rigorous' titiz veya zorlu demektir.", 'dynamic': "'Dynamic' hareketli/dinamik demektir.", 'strenuous': "'Strenuous' ağır efor gerektiren demektir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.syrup-01', 'vocab.syrup', 'B2',
    'Choose the correct pharmaceutical/sweet liquid noun:', 'Öksürük şurubu vb. yoğun sıvıyı anlatan doğru ismi seçiniz.', "The pediatrician prescribed a soothing herbal cough ___ to alleviate the child's chest irritation.",
    'syrup', ['powder', 'capsule', 'bandage'],
    "'Syrup' a thick, sticky liquid consisting of a concentrated solution of sugar and medication.", "'Syrup', tedavi edici tıbbi şuruptur.",
    {'powder': "'Powder' toz demektir.", 'capsule': "'Capsule' hap kapsülüdür.", 'bandage': "'Bandage' sargı bezidir."}, 'standard'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.account-for-01', 'vocab.account-for', 'C1',
    'Choose the correct phrasal verb meaning to explain or constitute a proportion:', 'Açıklamak veya bir oranı oluşturmak anlamına gelen doğru deyimsel fiili seçiniz.', "Renewable energy sources now ___ more than 40% of the nation's total electricity generation.",
    'account for', ['stand for', 'look after', 'break down'],
    "'Account for' to constitute, make up, or explain the cause of something.", "'Account for', bir oranı oluşturmak veya gerekçesini açıklamak demektir.",
    {'stand for': "'Stand for' temsil etmek veya hoşgörmek demektir.", 'look after': "'Look after' bakmak/ilgilenmek demektir.", 'break down': "'Break down' bozulmak veya parçalamak demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.biotechnology-01', 'vocab.biotechnology', 'C1',
    'Choose the correct cutting-edge scientific discipline noun:', 'Biyolojik sistemlerin teknolojik kullanımını ifade eden doğru terimi seçiniz.', 'Recent breakthroughs in ___ have revolutionized the development of targeted mRNA vaccines.',
    'biotechnology', ['astrology', 'archaeology', 'paleontology'],
    "'Biotechnology' the exploitation of biological processes for industrial and genetic purposes.", "'Biotechnology', genetik ve aşı alanında devrim yaratan biyoteknolojidir.",
    {'astrology': "'Astrology' astrolojidir (fal/burç).", 'archaeology': "'Archaeology' arkeolojidir.", 'paleontology': "'Paleontology' fosil bilimidir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.competently-01', 'vocab.competently', 'C1',
    'Choose the correct formal adverb meaning with skill and expertise:', 'Yetkin ve ehil bir şekilde eylemi gerçekleştirmeyi anlatan doğru zarfı seçiniz.', 'The senior systems engineer handled the multi-region failover crisis ___ and with complete composure.',
    'competently', ['recklessly', 'haphazardly', 'clumsily'],
    "'Competently' in an efficient and capable manner.", "'Competently', yetkin, ehil ve usta bir şekilde demektir.",
    {'recklessly': "'Recklessly' pervasızca demektir.", 'haphazardly': "'Haphazardly' gelişi güzel/baştan savma demektir.", 'clumsily': "'Clumsily' sakarca demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.deploy-01', 'vocab.deploy', 'C1',
    'Choose the correct technical verb for releasing software into production:', 'Yazılımı canlıya almayı veya devreye sokmayı anlatan doğru fiili seçiniz.', 'Our continuous integration pipeline automatically ___ validated builds directly to the production cluster.',
    'deploys', ['dismantles', 'terminates', 'scraps'],
    "'Deploy' to bring into effective action or release a software build to live servers.", "'Deploy', yazılımı canlı sunuculara yükleyip devreye almaktır.",
    {'dismantles': "'Dismantle' sökmek/parçalamak demektir.", 'terminates': "'Terminate' sonlandırmak demektir.", 'scraps': "'Scrap' hurdaya ayırmak demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.encryption-01', 'vocab.encryption', 'C1',
    'Choose the correct cryptographic security term:', 'Verileri şifreleyerek koruma altına alma teknolojisini anlatan doğru ismi seçiniz.', 'End-to-end ___ ensures that intercepted network packets cannot be deciphered by unauthorized parties.',
    'encryption', ['compression', 'corruption', 'duplication'],
    "'Encryption' the process of encoding messages or information in such a way that only authorized parties can access it.", "'Encryption', yetkisiz kişilerin veriyi okumasını engelleyen kriptografik şifrelemedir.",
    {'compression': "'Compression' dosya sıkıştırmadır.", 'corruption': "'Corruption' bozulma/yozlaşmadır.", 'duplication': "'Duplication' kopyalamadır."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.imperative-01', 'vocab.imperative', 'C1',
    'Choose the correct high-register noun denoting an absolute necessity:', 'Kaçınılmaz ve elzem bir zorunluluğu ifade eden doğru ismi seçiniz.', 'Ensuring consumer data privacy has transformed from a technical preference into a strict corporate ___ .',
    'imperative', ['suggestion', 'whim', 'distraction'],
    "'Imperative' an essential or urgent duty or unavoidable requirement.", "'Imperative', ertelenemez zorunluluk / elzem kural demektir.",
    {'suggestion': "'Suggestion' tavsiyedir.", 'whim': "'Whim' kapris/geçici hevestir.", 'distraction': "'Distraction' dikkat dağıtıcı unsurdur."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.microprocessor-01', 'vocab.microprocessor', 'C1',
    'Choose the correct semiconductor hardware component noun:', 'Bilgisayarın merkezi işlemci yongasını anlatan doğru ismi seçiniz.', 'The cutting-edge 3-nanometer ___ packs twenty billion transistors onto a single silicon die.',
    'microprocessor', ['resistor', 'insulator', 'capacitor'],
    "'Microprocessor' an integrated circuit that contains all the functions of a central processing unit of a computer.", "'Microprocessor', bilgisayarın beyni olan entegre mikroişlemcidir.",
    {'resistor': "'Resistor' elektrik direncidir.", 'insulator': "'Insulator' yalıtkandır.", 'capacitor': "'Capacitor' kondansatördür."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.posit-01', 'vocab.posit', 'C1',
    'Choose the correct academic verb meaning to put forward a hypothesis:', 'Tez veya varsayım olarak öne sürmeyi anlatan doğru akademik fiili seçiniz.', 'Contemporary evolutionary theorists ___ that cooperative social behaviors provided early hominids a survival advantage.',
    'posit', ['refute', 'repudiate', 'dismiss'],
    "'Posit' to put forward as fact or as a basis for argument.", "'Posit', bir sav veya tezi tartışmanın temeli olarak öne sürmektir.",
    {'refute': "'Refute' çürütmek demektir.", 'repudiate': "'Repudiate' reddetmek demektir.", 'dismiss': "'Dismiss' gözardı etmek demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.repurpose-01', 'vocab.repurpose', 'C1',
    'Choose the correct verb for adapting existing technology for a novel application:', 'Var olan bir teknolojiyi farklı bir amaç için yeniden değerlendirmeyi anlatan doğru fiili seçiniz.', 'The biomedical research team managed to ___ an existing malaria therapeutic to combat autoimmune disorders.',
    'repurpose', ['eradicate', 'demolish', 'deplete'],
    "'Repurpose' to adapt for use in a different purpose.", "'Repurpose', mevcut bir ilacı veya teknolojiyi dönüştürerek farklı bir amaçla kullanmaktır.",
    {'eradicate': "'Eradicate' kökünü kazımak demektir.", 'demolish': "'Demolish' yıkmak demektir.", 'deplete': "'Deplete' tüketmek demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.stylistically-01', 'vocab.stylistically', 'C1',
    'Choose the correct academic adverb evaluating rhetorical elegance:', 'Üslup ve biçim açısından değerlendirmeyi ifade eden doğru zarfı seçiniz.', 'The translation was accurate in literal meaning, but ___ it lacked the lyrical cadence of the original poetry.',
    'stylistically', ['statistically', 'chronologically', 'financially'],
    "'Stylistically' with regard to style or appearance, especially literary or artistic style.", "'Stylistically', üslup, biçim veya edebi tarz bakımından demektir.",
    {'statistically': "'Statistically' istatistiksel olarak demektir.", 'chronologically': "'Chronologically' kronolojik olarak demektir.", 'financially': "'Financially' mali açıdan demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.a-double-edged-sword-01', 'vocab.a-double-edged-sword', 'C2',
    'Choose the idiomatic metaphor for something having both favorable and perilous consequences:', 'Hem fayda hem büyük risk barındıran durumu anlatan doğru deyimi seçiniz.', 'Rapid generative AI adoption is ___ ; while it skyrockets productivity, it amplifies cybersecurity vulnerabilities.',
    'a double-edged sword', ['an open book', 'a silver spoon', 'a dead end'],
    "'A double-edged sword' something that has both beneficial and harmful consequences.", "'A double-edged sword' (iki ucu keskin bıçak), aynı anda hem büyük yarar hem ciddi tehlike barındıran durumdur.",
    {'an open book': 'Her şeyi açık ve gizlisiz olan kişi/durum demektir.', 'a silver spoon': 'Doğuştan varlıklı olmak demektir.', 'a dead end': 'Çıkmaz sokak demektir.'}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.body-of-knowledge-01', 'vocab.body-of-knowledge', 'C2',
    'Choose the comprehensive academic noun phrase for an accumulated domain canon:', 'Bir bilim veya uzmanlık dalına ait yapılandırılmış bilgi birikimini anlatan doğru tamlamayı seçiniz.', 'Her groundbreaking clinical trial contributed significantly to the established ___ on neuroplasticity.',
    'body of knowledge', ['state of play', 'rule of thumb', 'matter of opinion'],
    "'Body of knowledge' the complete set of concepts, terms, and activities that make up a professional domain.", "'Body of knowledge', bir disipline ait kapsamlı kurumsal bilgi birikimi ve külliyattır.",
    {'state of play': 'Olayların anlık mevcut durumu demektir.', 'rule of thumb': 'Pratik el yordamı kuralı demektir.', 'matter of opinion': 'Göreceli kişisel görüş meselesidir.'}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.degradable-01', 'vocab.degradable', 'C2',
    'Choose the exact material science adjective for environmentally breakable compounds:', 'Kimyasal ve biyolojik olarak doğada çözünebilen bileşikleri niteleyen doğru bilimsel sıfatı seçiniz.', 'The biotechnology lab engineered a novel polymer that is fully ___ in marine ecosystems within six weeks.',
    'degradable', ['indestructible', 'insoluble', 'inert'],
    "'Degradable' capable of being broken down into simpler chemical compounds naturally.", "'Degradable', biyolojik veya kimyasal olarak parçalanıp çözünebilen maddedir.",
    {'indestructible': "'Indestructible' yok edilemez demektir.", 'insoluble': "'Insoluble' çözünmez demektir.", 'inert': "'Inert' tepkimeye girmeyen demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.epistemology-01', 'vocab.epistemology', 'C2',
    'Choose the correct philosophical branch interrogating knowledge and truth:', 'Bilginin doğasını ve geçerliliğini inceleyen felsefe dalını anlatan doğru terimi seçiniz.', 'His dissertation explores the ___ of deep neural networks, questioning whether statistical correlation constitutes authentic understanding.',
    'epistemology', ['etymology', 'entomology', 'echocardiology'],
    "'Epistemology' the theory of knowledge, especially with regard to its methods, validity, and scope.", "'Epistemology', bilginin kökenini, sınırlarını ve geçerliliğini inceleyen bilgi felsefesidir.",
    {'etymology': "'Etymology' kelime köken bilimidir.", 'entomology': "'Entomology' böcek bilimidir.", 'echocardiology': "'Echocardiology' kalp ultrason bilimidir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.hegemony-01', 'vocab.hegemony', 'C2',
    'Choose the high-register geopolitical noun for supreme cultural or political dominance:', 'Küresel veya bölgesel mutlak hakimiyet ve egemenliği anlatan doğru kavramı seçiniz.', 'The rise of decentralized digital economies poses an unprecedented challenge to traditional sovereign currency ___ .',
    'hegemony', ['anarchy', 'reciprocity', 'feudalism'],
    "'Hegemony' leadership or predominant influence exercised by one nation or social group over others.", "'Hegemony', tahakküm, egemenlik ve siyasi/ekonomik üstünlük hegemonyasıdır.",
    {'anarchy': "'Anarchy' kuralsızlık/anarşidir.", 'reciprocity': "'Reciprocity' karşılıklılık ilkesidir.", 'feudalism': "'Feudalism' feodalizmdir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.ionize-01', 'vocab.ionize', 'C2',
    'Choose the precise physical chemistry verb for converting atoms into ions:', 'Atomları elektron kazandırarak veya kaybettirerek yüklü hale getirmeyi ifade eden bilimsel fiili seçiniz.', 'High-energy ultraviolet radiation has sufficient electromagnetic frequency to ___ atmospheric gas molecules.',
    'ionize', ['fossilize', 'galvanize', 'sterilize'],
    "'Ionize' to convert an atom, molecule, or substance into ions, typically by removing one or more electrons.", "'Ionize', atom veya molekülleri iyonlaştırmaktır.",
    {'fossilize': "'Fossilize' fosilleştirmek demektir.", 'galvanize': "'Galvanize' harekete geçirmek veya çinko kaplamaktır.", 'sterilize': "'Sterilize' mikroptan arındırmaktır."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.oligopoly-01', 'vocab.oligopoly', 'C2',
    'Choose the economic market structure noun dominated by a handful of corporate conglomerates:', 'Birkaç büyük firmanın pazara tamamen hakim olduğu piyasa yapısını anlatan doğru iktisat terimini seçiniz.', 'The commercial aerospace turbine industry is an entrenched ___ dominated by three global conglomerates.',
    'oligopoly', ['monopoly', 'meritocracy', 'kleptocracy'],
    "'Oligopoly' a state of limited competition, in which a market is shared by a small number of producers or sellers.", "'Oligopoly', az sayıda firmanın pazarı paylaştığı oligopol piyasa yapısıdır.",
    {'monopoly': "'Monopoly' tek firmanın hakim olduğu tekeldir.", 'meritocracy': "'Meritocracy' liyakat sistemidir.", 'kleptocracy': "'Kleptocracy' hırsızlar yönetimidir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.propensity-01', 'vocab.propensity', 'C2',
    'Choose the psychological noun denoting an innate inclination or tendency:', 'Doğuştan gelen güçlü bir eğilimi veya yatkınlığı anlatan doğru üst düzey ismi seçiniz.', "Behavioral economists have documented humanity's deep-seated ___ to overvalue immediate rewards over long-term gains.",
    'propensity', ['indifference', 'repugnance', 'reticence'],
    "'Propensity' an inclination or natural tendency to behave in a particular way.", "'Propensity', içsel bir meyil, yatkınlık veya eğilimdir.",
    {'indifference': "'Indifference' kayıtsızlık demektir.", 'repugnance': "'Repugnance' nefret/iğrenme demektir.", 'reticence': "'Reticence' ketumluk/çekingenlik demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.shore-up-01', 'vocab.shore-up', 'C2',
    'Choose the idiomatic phrasal verb meaning to reinforce or support from collapse:', 'Çökmekte olan bir yapıyı destekleyip ayakta tutmayı anlatan doğru deyimsel fiili seçiniz.', 'The finance ministry injected twenty billion euros into the regional banks to ___ market liquidity.',
    'shore up', ['tear down', 'phase out', 'scale back'],
    "'Shore up' to support, reinforce, or brace against potential collapse.", "'Shore up', çöküşü önlemek için takviye etmek, destekleyip ayakta tutmaktır.",
    {'tear down': "'Tear down' yerle bir etmek demektir.", 'phase out': "'Phase out' aşamalı olarak kaldırmak demektir.", 'scale back': "'Scale back' küçültmek demektir."}, 'stretch'))

VOCAB_EXERCISES.append(make_ex('exercise.vocab.teleological-01', 'vocab.teleological', 'C2',
    'Choose the philosophical adjective attributing phenomena to ultimate purpose or design:', 'Olayları amaçsal veya nihai bir ereğe dayandıran doğru felsefi sıfatı seçiniz.', 'The evolutionary biologist rejected the ___ explanation that eyes evolved specifically in order that humans might see.',
    'teleological', ['tautological', 'genealogical', 'pathological'],
    "'Teleological' relating to or involving the explanation of phenomena in terms of the purpose they serve rather than of the cause by which they arise.", "'Teleological', ereksel, amaçsal ve nihai gayeye dayanan felsefi yaklaşımdır.",
    {'tautological': "'Tautological' totolojik / gereksiz yinelemeli demektir.", 'genealogical': "'Genealogical' soybilimsel demektir.", 'pathological': "'Pathological' hastalıklı / patolojiktir."}, 'stretch'))

