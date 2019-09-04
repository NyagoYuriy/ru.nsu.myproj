#include <stdio.h>
#include <stdlib.h>
#include <string.h>


struct node {
    int sym;
    int freq;
    unsigned long long int code;
    int len_code;
    struct node* next;
    struct node* left, *right;
};
void Print_code (struct node*);


void push (struct node **head, int ch) {
    int flag_exist = 0;
    struct node *tmp = *head;

    if (!(*head)) {
        struct node *p = malloc(sizeof(struct node));
        p->sym = ch;
        p->next = NULL;
        p->freq = 1;
        p->left = NULL;
        p->right = NULL;
        (*head) = p;
    }
    else  {
        while (1) {    //checking existing ch in queue
            if (tmp->sym == ch) {
                tmp->freq++;
                flag_exist = 1;
                break;
            }
            if (tmp->next)
                tmp = tmp->next;
            else
                break;
        }
        if (!flag_exist) {
            struct node *p =(struct node *) malloc(sizeof(struct node));
            p->sym = ch;
            p->freq = 1;
            p->next = NULL;
            p->left = NULL;
            p->right = NULL;
            tmp->next = p;
        }

    }
}

void Frequency_count (FILE* input, struct node** head) {
   // unsigned char ch;
    int ch;
    while (!feof(input)) {

        ch = fgetc (input);
        if (feof(input))
            return;
        push (head, ch);
    }
}

void Push_in_order (struct node** new_head, struct node* p) {
    struct node* tmp = *new_head;
    if (!(*new_head)) {
        p->next = NULL;
        *new_head = p;
        return;
    }
    if (p->freq < (*new_head)->freq) {
        p->next = (*new_head);
        (*new_head) = p;
        return;
    }
    while (1) {
        if (tmp->next == NULL || (tmp->freq <= p->freq && tmp->next->freq >= p->freq)) {
            p->next = tmp->next;
            tmp->next = p;
            return;
        }
        tmp = tmp->next;
    }
}

struct node* Build_Sort_List (struct node* head) {
    struct node* new_head = NULL;
    struct node* temp;
    while (head){
        struct node* p = malloc(sizeof(struct node));
        p->freq = head->freq;
        p->sym = head->sym;
        Push_in_order(&new_head, p);
        temp = head;
        head = head->next;
        temp->next = NULL;
        free(temp);
    }
    return new_head;
}

void Copy_List (struct node* head, int arr[256]) {
    while (head) {
        if (head->sym != 300)
            arr[head->sym] = head;
        head = head->next;
    }
}


struct node* Build_Tree (struct node* head) {
    struct node* root = NULL, *tmp;
    while (head->next) {
        tmp = malloc (sizeof (struct node));
        tmp->freq = head->freq + head->next->freq;
        tmp->sym = 300; //просто так
        tmp->left = head;
        tmp->right = head->next;
        Push_in_order(&head, tmp);
        head = head->next->next;
    }
    return head;
}

int HeightOfTree(struct node* root) {
    if (root == NULL)
        return 0;
    int left, right;
    if (root->left != NULL) {
        left = HeightOfTree(root->left);
    }
    else
        left = -1;
    if (root->right != NULL) {
        right = HeightOfTree(root->right);
    }
    else
        right = -1;

    int max = left > right ? left : right;
    return max+1;

}

void GetCodes (struct node *root, unsigned long long int code, int Call_Number) {    //right - 1 left - 0
    unsigned long long int code_r, code_l;
    if (root->left) {
        code_l = code<<1;
        GetCodes (root->left, code_l, Call_Number+1);
    }
    if (root->right) {
        code_r = code<<1 | ((unsigned long long int)1);
        GetCodes (root->right, code_r, Call_Number+1);
    }
    if (root->sym != 300) {
        root->code = code;
        root->len_code = Call_Number;
       // printf ("%c ", root->sym);
       // Print_code(root);
        puts("\n");
    }
}


int Rewrite_File (FILE* input, FILE* output, struct node* mass[]) {
    int ch = 'a';
    unsigned char byte =0;
    int len_b, len_byte =0;
    unsigned long long int b;
    unsigned long long int sum_len  = 0 ;
    int i;
    while (1) {
        //ch = fgetc (input);
        fread (&ch, 1, 1, input);
        if (feof(input))
            break;
        len_b = mass[ch]->len_code;
        b = mass[ch]->code;
        sum_len++;
        for (i=len_b-1;i>=0;i--) {
            byte = byte | ((b>>i)&1)<<7-len_byte;
            len_byte++;
            if (len_byte == 8) {
                fwrite (&byte, sizeof (char), 1, output);
                len_byte = 0;
                byte =0;
            }
        }

    }
    if (len_byte){
        fwrite (&byte, sizeof (char), 1, output);
    }
   return sum_len;
}

void Restore_File (FILE* input, FILE* output, struct node* root, unsigned long long int sum_len) {
    unsigned char byte;
    int len_byte = 0;
    struct node* tmp = root;
    int direction;
    while (sum_len) {
        if (len_byte == 0)  {
            fread (&byte, 1,1, output);
            //printf ("\nbyte = %d\n", byte);
            len_byte = 8;
        }
        if (tmp->sym != 300) {
            fwrite (&tmp->sym, 1, 1, input);
            tmp = root;
            sum_len--;
        }

        direction = byte &  (1<<len_byte -1);
      // printf ("%d ", direction);
       if (direction)
            tmp = tmp->right;
        else
            tmp = tmp->left;
        len_byte--;

    }

}

int main() {
    FILE * input, *output;
    input = fopen ("Achieve IELTS CD2.zip", "rb");
    output = fopen ("output.my", "wb");
    struct node* head = NULL;
    Frequency_count(input, &head);
    int sum = 0;
    head = Build_Sort_List(head);
     struct node* tmp = head;
    while (tmp) {
        tmp->left = NULL;
        tmp->right = NULL;
        tmp = tmp->next;
    }
    struct node* mass[256] = {0};
    struct node* to_del;
    struct node* root;
    root = Build_Tree(head);
   // printf ("%d\n", HeightOfTree(root));
    GetCodes (root, (unsigned long long int)0, 0);
    Copy_List(head, mass);
    fclose (input);
/*
    int i;

    for (i=0;i<256;i++)
            printf ("%d %llu\n",i, mass[i]->code);
*/
    input = fopen ("Achieve IELTS CD2.zip", "rb");
    unsigned long long int sum_len;
    sum_len = Rewrite_File (input, output, mass);
    fclose (input);
    fclose (output);
    output = fopen ("output.my", "rb");
    input = fopen ("Achieve IELTS CD2_back.zip", "wb");
    Restore_File (input, output, root, sum_len);
   //printf ("%llu", sum_len);
puts ("\n\n");
/*
    while (head) {
        printf ("%d %d %llu\n", head->sym, head->freq, head->code);
        to_del = head;
        head = head->next;
        free (to_del);
    }
*/
    return 0;
}
